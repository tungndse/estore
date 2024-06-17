package com.coldev.estore.domain.dto.combo.response;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboGetDto {

    private Long id;

    private String name;

    private String description;

    @JsonProperty("discount_by_percent")
    private Double discountPercentage;

    @JsonProperty("discount_by_value")
    private BigDecimal discountValue;

    private Status status;

    @JsonProperty("img_url")
    private String imgUrl;

    //TODO Determine response, list of ProductGet or just Ids?
    @JsonProperty("products")
    private List<ProductGetDto> productGetDtoList;

}