package com.coldev.estore.domain.dto.product.request;


import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.SortType;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.FilterRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest extends FilterRequest {

    private Category category;

    private Double priceMin;

    private Double priceMax;

    private Status status;

    private Long quantityMin;


}
