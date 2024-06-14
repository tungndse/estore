package com.coldev.estore.domain.dto.product.response;


import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductGetDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long quantity;
    private Date createdAt;
    private Status status;
    private Category category;

}
