package com.coldev.estore.domain.dto.customerorder.response;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.enumerate.PaymentMethod;
import com.coldev.estore.common.serializer.MoneyBigDecimalSerializer;
import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.dto.customerorder.item.response.CustomerOrderItemGetDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderGetDto implements Serializable {

    private Long id;
    private String name;
    private String description;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    private OrderStatus status;

    @JsonProperty("payment_method")
    @JsonPropertyDescription("Default is COD, will support VNPAY")
    private PaymentMethod paymentMethod;

    @JsonProperty("discount_total")
    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    private BigDecimal discountTotal;

    @JsonProperty("total_amount")
    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    private BigDecimal totalAmount;

    @JsonProperty("net_amount")
    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    private BigDecimal netAmount;

    @JsonProperty("order_date")
    @JsonPropertyDescription("Date that the order is placed")
    private Date orderDate;

    @JsonProperty("created_at")
    @JsonPropertyDescription("Date that the order is created on db, but not placed yet")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("customer")
    private AccountGetDto customerGetDto;

    @JsonProperty("customer_order_items")
    private List<CustomerOrderItemGetDto> customerOrderItemGetDtoList;

    @JsonProperty("is_for_combo")
    private Boolean isComboOrder;

    @JsonProperty("for_combo")
    private ComboGetDto comboGetDto;
}
