package com.coldev.estore.domain.dto.customerorder.request;


import com.coldev.estore.common.enumerate.PaymentMethod;
import com.coldev.estore.domain.dto.customerorder.item.request.ComboItemPostDto;
import com.coldev.estore.domain.dto.customerorder.item.request.CustomerOrderItemPostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderRequestPayload {

    @JsonProperty("customer_id")
    private Long accountId;

    private String description;

    private String shipping_address;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("customer_order_item_list")
    private List<CustomerOrderItemPostDto> customerOrderItemList;

    @JsonProperty("combo_item_list")
    private List<ComboItemPostDto> comboItemList;

}
