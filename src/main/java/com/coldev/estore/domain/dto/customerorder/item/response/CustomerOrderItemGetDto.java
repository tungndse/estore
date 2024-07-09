package com.coldev.estore.domain.dto.customerorder.item.response;

import com.coldev.estore.common.serializer.MoneyBigDecimalSerializer;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderItemGetDto implements Serializable {

    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    private Long quantity;

    @JsonProperty("unit_price")
    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    private BigDecimal unitPrice;


    @JsonProperty("total_price")
    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    private BigDecimal totalPrice;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("product")
    private ProductGetDto productGetDto;



}
