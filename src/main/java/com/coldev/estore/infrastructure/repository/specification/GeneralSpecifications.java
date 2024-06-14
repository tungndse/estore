package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.FilterRequest;
import org.springframework.data.jpa.domain.Specification;

public class GeneralSpecifications {

    public static <E> Specification<E> searchByName(FilterRequest filterRequest) {
        String searchKey = filterRequest.getSearchKey();

        if (searchKey == null || searchKey.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchKey.toLowerCase() + "%");
        };
    }

    public static <E> Specification<E> descriptionContainsString(FilterRequest filter) {
        String stringContains = filter.getDescriptionContainsString();

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("description")),
                            "%" + stringContains.toLowerCase() + "%");
        };
    }

}