package com.coldev.estore.domain.dto.product.request;


import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest extends FilterRequest implements Serializable {

    private Category category;

    private Double priceMin;

    private Double priceMax;

    private Status status;

    private Long quantityMin;


}
