package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.entity.Combo;
import com.coldev.estore.domain.entity.Product;
import com.coldev.estore.domain.entity.ProductCombo;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ComboSpecifications {

    public static Specification<Combo> equalsToAnyId(Set<Long> ids) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("id")).value(ids)));
    }

    public static Specification<Combo> hasDiscountPercentGreaterThanOrEqualTo(Double discountPercentMin) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("discountPercent"),
                        discountPercentMin
                )
        ));
    }

    public static Specification<Combo> hasDiscountPercentLessThanOrEqualTo(Double discountPercentMax) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get("discountPercent"),
                discountPercentMax
        )));
    }

    public static Specification<Combo> hasDiscountValueGreaterThanOrEqualTo(BigDecimal discountValueMin) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("discountValue"), discountValueMin)));
    }

    public static Specification<Combo> hasDiscountValueLessThanOrEqualTo(BigDecimal discountValueMax) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("discountValue"), discountValueMax)));
    }

    public static Specification<Combo> hasAnyStatuses(Set<Status> statuses) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("status")).value(statuses)
        ));
    }

    public static Specification<Combo> hasProductId(Long productId) {
        return (root, query, criteriaBuilder) -> {
            Join<Combo, Product> productJoin = root.join("products");
            return criteriaBuilder.equal(productJoin.get("id"), productId);
        };
    }

    public static Specification<Combo> hasProductCount(Long count) {
        return (root, query, criteriaBuilder) -> {
            // Create a subquery to count the number of products associated with each combo
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Combo> subRoot = subquery.from(Combo.class);
            subquery.select(criteriaBuilder.count(subRoot.get("id")));

            // Join with the Product entity through the products collection
            subRoot.join("products");

            // Add the where clause to filter by the combo ID
            subquery.where(criteriaBuilder.equal(subRoot.get("id"), root.get("id")));

            // Add the having clause to filter by the product count
            return criteriaBuilder.equal(subquery, count);
        };
    }





}
