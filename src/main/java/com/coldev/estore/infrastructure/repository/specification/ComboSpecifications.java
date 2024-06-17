package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.entity.Combo;
import com.coldev.estore.domain.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class ComboSpecifications {

    public static Specification<Combo> equalsToAnyId(Set<Long> ids) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("id")).value(ids))
        ));
    }

}
