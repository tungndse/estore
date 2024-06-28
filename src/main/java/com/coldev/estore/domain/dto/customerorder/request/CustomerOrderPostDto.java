package com.coldev.estore.domain.dto.customerorder.request;

import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.enumerate.PaymentMethod;
import com.coldev.estore.domain.dto.customerorder.item.request.CustomerOrderItemPostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderPostDto implements Serializable {

    @JsonProperty("customer_id")
    //@NotNull(message = MessageDictionary.ACCOUNT_IS_REQUIRED)
    private Long accountId;

    private String description;

    private OrderStatus status;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("is_combo_oder")
    private Boolean isComboOrder;

    // If combo is present, not necessary for customer order item list
    @JsonProperty("combo_id")
    private Long comboId;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("customer_order_items")
    private List<CustomerOrderItemPostDto> customerOrderItemPostDtoList;

}
