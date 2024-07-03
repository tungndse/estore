package com.coldev.estore.config.exception.mapper;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.domain.dto.customerorder.item.request.CustomerOrderItemPostDto;
import com.coldev.estore.domain.dto.customerorder.item.response.CustomerOrderItemGetDto;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderRequestPayload;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.entity.CustomerOrderItem;
import com.coldev.estore.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.Date;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
        , uses = {AccountMapper.class, ComboMapper.class, ProductMapper.class}
)
public interface CustomerOrderMapper {

    default CustomerOrderItem.CustomerOrderItemBuilder toNewCustomerOrderItemBuilder
            (CustomerOrderItemPostDto customerOrderItemPostDto) {
        if (customerOrderItemPostDto == null) return null;

        return CustomerOrderItem.builder()
                .createdAt(new Date());
    }


    default CustomerOrderItem.CustomerOrderItemBuilder toNewCustomerOrderItemBuilder
            (Product product) {
        if (product == null) return null;

        return CustomerOrderItem.builder()
                .product(product)
                .unitPrice(product.getPrice())
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .createdAt(new Date());


    }

    default CustomerOrder.CustomerOrderBuilder toNewCustomerOrderBuilder
            (CustomerOrderRequestPayload customerOrderRequestPayload) {
        if (customerOrderRequestPayload == null) return null;

        return CustomerOrder.builder()
                .createdAt(new Date())
                .paymentMethod(customerOrderRequestPayload.getPaymentMethod())
                .description(customerOrderRequestPayload.getDescription())
                .shippingAddress(customerOrderRequestPayload.getShipping_address())
                .status(OrderStatus.ON_HOLD);


    }

    default CustomerOrderGetDto.CustomerOrderGetDtoBuilder toCustomerOrderGetDtoBuilder(CustomerOrder customerOrder) {
        if (customerOrder == null) return null;

        return CustomerOrderGetDto.builder()
                .id(customerOrder.getId())
                .status(customerOrder.getStatus())
                .createdAt(customerOrder.getCreatedAt())
                .paymentMethod(customerOrder.getPaymentMethod())
                .description(customerOrder.getDescription())
                .shippingAddress(customerOrder.getShippingAddress())
                .orderDate(customerOrder.getOrderDate())
                .totalAmount(customerOrder.getTotalAmount())
                .discountTotal(customerOrder.getDiscountTotal())
                .netAmount(customerOrder.getNetAmount())
                .isComboOrder(customerOrder.getIsComboOrder())
                .customerGetDto(AccountMapper.INSTANCE.toAccountGetDto(customerOrder.getCustomer()))
                .comboGetDto(customerOrder.getCombo() != null ?
                        ComboMapper.INSTANCE.toComboGetDto(customerOrder.getCombo()) : null)
                ;
    }

    default CustomerOrderItemGetDto toCustomerOrderItemGetDto(CustomerOrderItem customerOrderItem) {
        if (customerOrderItem == null) return null;

        return CustomerOrderItemGetDto.builder()
                .id(customerOrderItem.getId())
                .createdAt(customerOrderItem.getCreatedAt())
                .orderId(customerOrderItem.getCustomerOrder() != null ?
                        customerOrderItem.getCustomerOrder().getId() : null)
                .quantity(customerOrderItem.getQuantity())
                .productGetDto(ProductMapper.INSTANCE.toProductGetDtoBuilder(customerOrderItem.getProduct()).build())
                .unitPrice(customerOrderItem.getUnitPrice())
                .totalPrice(customerOrderItem.getTotalPrice())
                .build();
    }
}
