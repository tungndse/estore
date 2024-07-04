package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.utility.CalculatorUtils;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.general.ItemUnavailableException;
import com.coldev.estore.config.exception.mapper.CustomerOrderMapper;
import com.coldev.estore.domain.dto.customerorder.item.request.ComboItemPostDto;
import com.coldev.estore.domain.dto.customerorder.item.request.CustomerOrderItemPostDto;
import com.coldev.estore.domain.dto.customerorder.item.response.CustomerOrderItemGetDto;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderRequestPayload;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.entity.*;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.ComboService;
import com.coldev.estore.domain.service.CustomerOrderService;
import com.coldev.estore.domain.service.ProductService;
import com.coldev.estore.infrastructure.repository.CustomerOrderItemRepository;
import com.coldev.estore.infrastructure.repository.CustomerOrderRepository;
import com.coldev.estore.infrastructure.repository.specification.CustomerOrderItemSpecifications;
import com.coldev.estore.infrastructure.repository.specification.ProductSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
@Log4j2
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final CustomerOrderMapper customerOrderMapper;

    private final AccountService accountService;
    private final ProductService productService;
    private final ComboService comboService;

    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerOrderItemRepository customerOrderItemRepository;

    public CustomerOrderServiceImpl(CustomerOrderMapper customerOrderMapper, AccountService accountService, ProductService productService, CustomerOrderRepository customerOrderRepository, CustomerOrderItemRepository customerOrderItemRepository, ComboService comboService) {
        this.customerOrderMapper = customerOrderMapper;
        this.accountService = accountService;
        this.productService = productService;
        this.customerOrderRepository = customerOrderRepository;
        this.customerOrderItemRepository = customerOrderItemRepository;
        this.comboService = comboService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> createCustomerOrders(CustomerOrderRequestPayload customerOrderRequestPayload) {

        // Check the account existence
        Account customer = accountService.getAccountByIdWithNullCheck(customerOrderRequestPayload.getAccountId());

        //
        List<CustomerOrder> customerOrders = new ArrayList<>();

        // TODO: Process the item list -> 1 Customer Order
        if (customerOrderRequestPayload.getCustomerOrderItemList() != null
                && !customerOrderRequestPayload.getCustomerOrderItemList().isEmpty()) {

            List<CustomerOrderItem> customerOrderItemList = this
                    .prepCustomerOrderItemListWithNullCustomerOrder
                            (customerOrderRequestPayload.getCustomerOrderItemList());

            CustomerOrder.CustomerOrderBuilder customerOrderBuilder =
                    customerOrderMapper.toNewCustomerOrderBuilder(customerOrderRequestPayload)
                            .customerOrderItems(customerOrderItemList)
                            .customer(customer)
                            .isComboOrder(false);

            BigDecimal totalAmount = CalculatorUtils.sum(
                    customerOrderItemList.stream()
                            .map(CustomerOrderItem::getTotalPrice)
                            .toList()
            );

            CustomerOrder customerOrder = customerOrderBuilder
                    .totalAmount(totalAmount)
                    .discountTotal(BigDecimal.ZERO)
                    .netAmount(totalAmount)
                    .build();

            customerOrders.add(customerOrder);

        } // End of item list processing

        // TODO: Check the combo list -> Multiple Customer Orders
        if (customerOrderRequestPayload.getComboItemList() != null
                && !customerOrderRequestPayload.getComboItemList().isEmpty()) {

            for (ComboItemPostDto comboItemPostDto : customerOrderRequestPayload.getComboItemList()) {
                Combo combo = comboService.getComboByIdWithAvailabilityCheck(comboItemPostDto.getId());

                List<CustomerOrderItem> customerOrderItemList = this
                        .prepCustomerOrderItemListWithNullCustomerOrder(combo, comboItemPostDto.getQuantity());

                CustomerOrder.CustomerOrderBuilder customerOrderBuilder =
                        customerOrderMapper.toNewCustomerOrderBuilder(customerOrderRequestPayload)
                                .customerOrderItems(customerOrderItemList)
                                .customer(customer)
                                .isComboOrder(true);
                BigDecimal orderTotalAmount = CalculatorUtils.sum(
                        customerOrderItemList.stream()
                                .map(CustomerOrderItem::getTotalPrice)
                                .toList()
                );

                BigDecimal orderComboDiscount = BigDecimal.ZERO;

                if (combo.getDiscountValue() != null) {
                    orderComboDiscount = orderComboDiscount.add(combo.getDiscountValue());
                }

                if (combo.getDiscountPercentage() != null) {
                    orderComboDiscount = orderComboDiscount.add(
                            orderTotalAmount.multiply(BigDecimal.valueOf(combo.getDiscountPercentage()))
                    );
                }

                BigDecimal orderNetAmount = BigDecimal.ZERO;
                if (orderTotalAmount.compareTo(orderComboDiscount) > 0) {
                    orderNetAmount = orderTotalAmount.subtract(orderComboDiscount);
                }

                CustomerOrder customerOrder = customerOrderBuilder
                        .totalAmount(orderTotalAmount)
                        .discountTotal(orderComboDiscount)
                        .netAmount(orderNetAmount)
                        .build();

                customerOrders.add(customerOrder);

            } // End of combo processing

        }

        // TODO: Traverse all CustomerOrders for every CustomerOrderItems and populate the hashmap (productId, size)
        // Map(productId, size)
        Map<Product, Long> productQuantityMap = new HashMap<>();
        for (CustomerOrder customerOrder : customerOrders) {
            List<CustomerOrderItem> customerOrderItemList = customerOrder.getCustomerOrderItems();
            for (CustomerOrderItem customerOrderItem : customerOrderItemList) {

                Product product = customerOrderItem.getProduct();
                Long productQuantity = customerOrderItem.getQuantity();

                productQuantityMap.merge(product, productQuantity, Long::sum);
            }
        }

        //Long newQuantity = product.getQuantity() - orderedQuantity;
        productQuantityMap.forEach(productService::updateProductQuantity);

        List<Long> savedCustomerOrderIdList = new ArrayList<>();

        // TODO: be done in the final
        for (CustomerOrder customerOrder : customerOrders) {
            CustomerOrder newCustomerOrder = customerOrderRepository.save(customerOrder);
            for (CustomerOrderItem customerOrderItem : customerOrder.getCustomerOrderItems()) {
                customerOrderItem.setCustomerOrder(newCustomerOrder);
            }
            customerOrderItemRepository.saveAll(customerOrder.getCustomerOrderItems());
            savedCustomerOrderIdList.add(newCustomerOrder.getId());
        }

        return savedCustomerOrderIdList;
    }

    private List<CustomerOrderItem> prepCustomerOrderItemListWithNullCustomerOrder
            (List<CustomerOrderItemPostDto> customerOrderItemRequestList) {

        List<CustomerOrderItem> customerOrderItemList
                = new ArrayList<>();

        for (CustomerOrderItemPostDto itemPostDto : customerOrderItemRequestList) {

            // Check Product Availability: Check quantity > 0 && ACTIVE status
            Product product = productService
                    .getProductByIdWithAvailabilityCheck(itemPostDto.getProductId());

            // Date Created into builder
            CustomerOrderItem.CustomerOrderItemBuilder itemBuilder =
                    customerOrderMapper.toNewCustomerOrderItemBuilder(itemPostDto)
                            .product(product);

            // Quantity
            Long orderedProductQuantity = itemPostDto.getQuantity();
            itemBuilder.quantity(orderedProductQuantity);

            // Unit Price
            itemBuilder.unitPrice(product.getPrice());

            // Total Price
            BigDecimal totalItemPrice = product.getPrice().multiply(BigDecimal.valueOf(orderedProductQuantity));
            itemBuilder.totalPrice(totalItemPrice);

            CustomerOrderItem customerOrderItem = itemBuilder.build();
            customerOrderItemList.add(customerOrderItem);

        } // End for

        return customerOrderItemList;
    }

    private List<CustomerOrderItem> prepCustomerOrderItemListWithNullCustomerOrder(Combo combo, Long comboOrderedQuantity) {

        List<CustomerOrderItem> customerOrderItemList = new ArrayList<>();

        List<Product> productListOfCombo = productService.getProductList(
                ProductSpecifications.hasComboId(combo.getId())
        );

        if (productListOfCombo == null || productListOfCombo.isEmpty())
            throw new ItemUnavailableException(combo.getId(), ConstantDictionary.COMBO);

        for (Product product : productListOfCombo) {
            if (!productService.checkProductAvailability(product))
                throw new ItemUnavailableException(product.getId(), ConstantDictionary.PRODUCT);

            CustomerOrderItem.CustomerOrderItemBuilder itemBuilder = customerOrderMapper
                    .toNewCustomerOrderItemBuilder(product)
                    .quantity(comboOrderedQuantity);

            CustomerOrderItem customerOrderItem = itemBuilder.build();

            customerOrderItemList.add(customerOrderItem);
        }

        return customerOrderItemList;

    }

    // TODO: Check for VNPay payment here?
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> placeCustomerOrders(List<Long> customerOrderIdList) {
        for (Long customerOrderId : customerOrderIdList) {
            CustomerOrder customerOrder = this.getCustomerOrderByIdWithNullCheck(customerOrderId);
            customerOrder.setStatus(OrderStatus.PENDING);
            customerOrder.setOrderDate(new Date());
            customerOrderRepository.save(customerOrder);
        }

        return customerOrderIdList;
    }

    @Override
    public List<CustomerOrderGetDto> getCustomerOrderDtoListByIds(List<Long> createdCustomerOrderIdList) {

        return createdCustomerOrderIdList.stream()
                .map(customerOrderId -> {
                    CustomerOrder customerOrder = this.getCustomerOrderByIdWithNullCheck(customerOrderId);
                    CustomerOrderGetDto.CustomerOrderGetDtoBuilder customerOrderGetDtoBuilder =
                            customerOrderMapper.toCustomerOrderGetDtoBuilder(customerOrder);

                    List<CustomerOrderItem> customerOrderItemList =
                            customerOrderItemRepository.findAll(
                                    CustomerOrderItemSpecifications.hasCustomerOrderId(customerOrderId)
                            );

                    if (!customerOrderItemList.isEmpty()) {

                        List<CustomerOrderItemGetDto> customerOrderItemGetDtoList =
                                customerOrderItemList.stream()
                                        .map(customerOrderMapper::toCustomerOrderItemGetDto)
                                        .toList();

                        customerOrderGetDtoBuilder.customerOrderItemGetDtoList(customerOrderItemGetDtoList);
                    }

                    return customerOrderGetDtoBuilder.build();
                })
                .toList();

    }

    @Override
    public CustomerOrder getCustomerOrderById(Long id) {
        return customerOrderRepository.findById(id).orElse(null);
    }

    @Override
    public CustomerOrder getCustomerOrderByIdWithNullCheck(Long id) {
        CustomerOrder customerOrder = this.getCustomerOrderById(id);

        if (customerOrder == null) throw new ItemNotFoundException(id, ConstantDictionary.CUSTOMER_ORDER);

        return customerOrder;
    }
}
