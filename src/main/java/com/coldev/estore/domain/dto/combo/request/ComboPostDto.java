package com.coldev.estore.domain.dto.combo.request;


import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboPostDto {

    @JsonIgnore
    private Long id;

    private String name;

    private String description;

    @JsonProperty("discount_by_percent")
    private Double discountPercentage;

    @JsonProperty("discount_by_value")
    private BigDecimal discountValue;
    private Status status;

    @JsonProperty("main_media_id")
    private Long mainMediaId;

    @JsonProperty("product_ids")
    private Set<Long> productIds;


}
