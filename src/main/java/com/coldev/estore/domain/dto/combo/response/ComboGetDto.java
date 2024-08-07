package com.coldev.estore.domain.dto.combo.response;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.common.serializer.MoneyBigDecimalSerializer;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboGetDto implements Serializable {

    private Long id;

    private String name;

    private String description;

    //@JsonProperty("discount_by_percent")
    @JsonIgnore
    private Double discountPercentage;

    @JsonProperty("discount_by_percent")
    private String discountPercentageString;

    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    @JsonProperty("discount_by_value")
    private BigDecimal discountValue;

    private Status status;

    @JsonProperty("img_url")
    private String imgUrl;

    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    @JsonProperty("products_total")
    private BigDecimal productsTotal;

    @JsonProperty("products")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductGetDto> productGetDtoList;

    public String getDiscountPercentageString() {
        if (discountPercentage == null || discountPercentage == 0.0) return null;

        return discountPercentage * 100 + "%";
    }
}
