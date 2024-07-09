package com.coldev.estore.domain.dto.customerorder.request;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.enumerate.PaymentMethod;
import com.coldev.estore.domain.dto.customerorder.item.request.ComboItemPostDto;
import com.coldev.estore.domain.dto.customerorder.item.request.CustomerOrderItemPostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("product_list")
    private List<CustomerOrderItemPostDto> customerOrderItemPostDtoList;

    @JsonProperty("combo_list")
    private List<ComboItemPostDto> comboItemPostDtoList;


}
