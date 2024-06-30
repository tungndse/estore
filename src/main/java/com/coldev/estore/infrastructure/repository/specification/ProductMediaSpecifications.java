package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.entity.ProductCombo;
import com.coldev.estore.domain.entity.ProductMedia;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class ProductMediaSpecifications {

    public static Specification<ProductMedia> hasProductId(Long id) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productId"), id)));
    }

    public static Specification<ProductMedia> equalsToAnyMediaId(Set<Long> ids) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("mediaId")).value(ids))
        ));
    }

}
