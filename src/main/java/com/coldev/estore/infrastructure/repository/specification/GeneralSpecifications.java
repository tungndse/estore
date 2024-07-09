package com.coldev.estore.infrastructure.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public class GeneralSpecifications {

    /*public static <E> Specification<E> nameContains(FilterRequest filterRequest) {
        String searchKey = filterRequest.getSearchKey();

        if (searchKey == null || searchKey.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchKey.toLowerCase() + "%");
        };
    }*/

    public static <E> Specification<E> nameContains(String searchKey) {
        if (searchKey == null || searchKey.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchKey.toLowerCase() + "%");
        };
    }

    /*public static <E> Specification<E> descriptionContains(FilterRequest filter) {
        String stringContains = filter.getDescriptionContains();

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("description")),
                            "%" + stringContains.toLowerCase() + "%");
        };
    }*/

    public static <E> Specification<E> descriptionContains(String stringContains) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("description")),
                            "%" + stringContains.toLowerCase() + "%");
        };
    }

}
