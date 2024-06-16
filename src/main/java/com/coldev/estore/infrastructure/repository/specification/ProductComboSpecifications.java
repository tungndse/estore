package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.entity.ProductCombo;
import org.springframework.data.jpa.domain.Specification;

public class ProductComboSpecifications {

    public static Specification<ProductCombo> hasComboId(Long comboId) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("comboId"), comboId)));
    }

}
