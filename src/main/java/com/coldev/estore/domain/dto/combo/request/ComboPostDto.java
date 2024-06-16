package com.coldev.estore.domain.dto.combo.request;


import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboPostDto {

    //private Long id;

    private String name;

    private String description;

    @JsonProperty("discount_by_percent")
    private Double discountPercentage;

    @JsonProperty("discount_by_value")
    private BigDecimal discountValue;
    private Status status;

    @JsonProperty("img_url")
    private String imgUrl;

    @JsonProperty("product_ids")
    private Set<Long> productIds;


}
