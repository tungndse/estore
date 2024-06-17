package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.entity.ProductMedia;
import org.springframework.data.jpa.domain.Specification;

public class ProductMediaSpecifications {

    public static Specification<ProductMedia> hasProductId(Long id) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productId"), id)));
    }

}
