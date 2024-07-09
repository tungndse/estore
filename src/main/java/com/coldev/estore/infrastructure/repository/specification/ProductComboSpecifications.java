package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.entity.ProductCombo;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class ProductComboSpecifications {

    public static Specification<ProductCombo> hasComboId(Long comboId) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("comboId"), comboId)));
    }

    public static Specification<ProductCombo> hasProductId(Long productId) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productId"), productId)));
    }

    public static Specification<ProductCombo> equalsToAnyId(Set<Long> ids) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("id")).value(ids))
        ));
    }

    public static Specification<ProductCombo> equalsToAnyProductId(Set<Long> ids) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("productId")).value(ids))
        ));
    }

}
