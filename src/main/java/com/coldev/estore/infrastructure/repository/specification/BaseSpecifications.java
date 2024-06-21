package com.coldev.estore.infrastructure.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class BaseSpecifications<T> {

    public  Specification<T> equalsToAnyId(Set<Long> ids) {

        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("id")).value(ids)));

    }

}
