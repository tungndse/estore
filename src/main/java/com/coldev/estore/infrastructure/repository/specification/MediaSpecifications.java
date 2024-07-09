package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.entity.Media;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class MediaSpecifications {
    public static Specification<Media> equalsToAnyId(Set<Long> ids) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("id")).value(ids))
        ));
    }
}
