package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.config.exception.mapper.AccountMapper;
import com.coldev.estore.config.exception.mapper.CustomerOrderMapper;
import com.coldev.estore.config.exception.mapper.ProductMapper;
import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.dto.dashboard.response.Dashboard;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.entity.CustomerOrderItem;
import com.coldev.estore.domain.entity.Product;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.CustomerOrderService;
import com.coldev.estore.domain.service.DashboardService;
import com.coldev.estore.infrastructure.repository.specification.CustomerOrderSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DashboardServiceImpl implements DashboardService {

    private final CustomerOrderMapper customerOrderMapper;
    private final AccountMapper accountMapper;
    private final ProductMapper productMapper;
    private final CustomerOrderService customerOrderService;
    private final AccountService accountService;


    public DashboardServiceImpl(CustomerOrderMapper customerOrderMapper, AccountMapper accountMapper, ProductMapper productMapper, CustomerOrderService customerOrderService, AccountService accountService) {
        this.customerOrderMapper = customerOrderMapper;
        this.accountMapper = accountMapper;
        this.productMapper = productMapper;
        this.customerOrderService = customerOrderService;
        this.accountService = accountService;
    }


    @Override
    public Dashboard loadDashboard(Month month, Year year) {

        Specification<CustomerOrder> specification = this
                .prepCustomerOrderSpecification(month, year)
                .and(CustomerOrderSpecifications.hasStatus(OrderStatus.COMPLETED));

        List<CustomerOrder> customerOrders = customerOrderService
                .findCustomerOrderListBySpecification(specification);

        // Return an empty Dashboard if order list is empty
        if (customerOrders.isEmpty())
            return Dashboard.builder().month(month).year(year).build();

        // Retrieve and group CustomerOrderItem objects by their CustomerOrder ID
        Map<Long, List<CustomerOrderItem>> CustomerOrderItemGroupedByCustomerOrder = this
                .retrieveAndGroupOrderItems(customerOrders);

        // Set CustomerOrderItems for each CustomerOrder
        for (CustomerOrder customerOrder : customerOrders) {
            List<CustomerOrderItem> customerOrderItemList = CustomerOrderItemGroupedByCustomerOrder
                    .getOrDefault(customerOrder.getId(), Collections.emptyList());
            customerOrder.setCustomerOrderItems(customerOrderItemList);
        }

        // Calculate total income
        BigDecimal totalIncome = customerOrders.stream()
                .map(CustomerOrder::getNetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate product sold count
        Long productSoldCount = customerOrders.stream()
                .flatMap(
                        order -> order.getCustomerOrderItems().stream()
                )
                .mapToLong(CustomerOrderItem::getQuantity)
                .sum();

        // Convert CustomerOrder entities to DTOs in a custom sort
        List<CustomerOrderGetDto> customerOrderGetDtoList = customerOrders.stream()
                .sorted(Comparator.comparing(CustomerOrder::getOrderDate).reversed())
                .map(order -> customerOrderMapper.toCustomerOrderGetDtoBuilder(order).build())
                .toList();

        // Find the top customer (by total amount spent)
        AccountGetDto topCustomer = this.findTopCustomerGetDto(customerOrders);

        // Find top 5 products ordered
        List<ProductGetDto> topFiveProductGetDtoList = this.findTopFiveProductGetDtoList(customerOrders);

        // Build and return the dashboard
        return Dashboard.builder()
                .month(month)
                .year(year)
                .total(totalIncome)
                .productSoldCount(productSoldCount)
                .topCustomer(topCustomer)
                .customerOrderGetDtoList(customerOrderGetDtoList)
                .topFiveProductDtoList(topFiveProductGetDtoList)
                .build();
    }

    private List<ProductGetDto> findTopFiveProductGetDtoList(List<CustomerOrder> customerOrders) {
        return customerOrders.stream()
                .flatMap(order -> order.getCustomerOrderItems().stream())
                .collect(
                        Collectors.groupingBy(
                                CustomerOrderItem::getProduct,
                                Collectors.summingLong(CustomerOrderItem::getQuantity)
                        )
                )
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> productMapper.toProductGetDtoBuilder(entry.getKey()).build())
                .toList();
    }

    private AccountGetDto findTopCustomerGetDto(List<CustomerOrder> customerOrders) {

        return customerOrders.stream()
                .collect(// Map Account and their corresponding spent amount (net amount)
                        Collectors.groupingBy(
                                CustomerOrder::getCustomer,
                                Collectors.reducing(BigDecimal.ZERO, CustomerOrder::getNetAmount, BigDecimal::add)
                        )
                )
                // Open entrySet
                .entrySet().stream()
                // Compare Values in entry set to get the entry with top value (max)
                .max(Comparator.comparing(Map.Entry::getValue))
                // Grab the Top Entry and convert to AccountGetDto
                .map(entry -> accountMapper.toAccountGetDto(entry.getKey()))
                .orElse(null);
    }

    private Map<Long, List<CustomerOrderItem>> retrieveAndGroupOrderItems(List<CustomerOrder> customerOrders) {
        // Collect all CustomerOrder IDs
        List<Long> customerOrderIdList = customerOrders.stream()
                .map(CustomerOrder::getId)
                .collect(Collectors.toList());

        // Fetch all CustomerOrderItems in a single query
        List<CustomerOrderItem> allCustomerOrderItems =
                customerOrderService.getCustomerOrderItemListByCustomerOrderIdList(customerOrderIdList);

        // Group CustomerOrderItems by CustomerOrder ID and return
        return allCustomerOrderItems.stream()
                .collect(Collectors.groupingBy(
                        (CustomerOrderItem customerOrderItem) ->
                                customerOrderItem.getCustomerOrder().getId()
                ));
    }

    private Specification<CustomerOrder> prepCustomerOrderSpecification(Month month, Year year) {
        Specification<CustomerOrder> specification = Specification.where(null);

        if (month != null && year != null) {
            // month != null & year != null
            specification = specification
                    .and(CustomerOrderSpecifications.hasYear(year.getValue()))
                    .and(CustomerOrderSpecifications.hasMonth(month.getValue()));
        } else if (year != null) {
            // month = null & year != null
            specification = specification.and(CustomerOrderSpecifications.hasYear(year.getValue()));
        } else if (month == null) {
            // month = null & year = null
            log.info("No year or month input");
        } else {
            // month != null & year = null
            specification = specification.and(CustomerOrderSpecifications.hasCurrentYear())
                    .and(CustomerOrderSpecifications.hasMonth(month.getValue()));
        }

        return specification;
    }


}
