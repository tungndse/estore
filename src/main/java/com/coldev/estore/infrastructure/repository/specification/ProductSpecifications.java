package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.entity.Combo;
import com.coldev.estore.domain.entity.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class ProductSpecifications {

    public static Specification<Product> hasCategory(ProductFilterRequest filterRequest) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), filterRequest.getCategory()));
    }

    public static Specification<Product> hasCategory(Category category) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category));
    }

    public static Specification<Product> filterByPrices(ProductFilterRequest productFilterRequest) {
        if (productFilterRequest.getPriceMin() == null && productFilterRequest.getPriceMax() == null) {
            return null;
        } else if (productFilterRequest.getPriceMax() == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), productFilterRequest.getPriceMin());
        } else if (productFilterRequest.getPriceMin() == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), productFilterRequest.getPriceMax());
        } else {
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price"), productFilterRequest.getPriceMin(), productFilterRequest.getPriceMax());
        }
    }

    public static Specification<Product> filterByPrices(Double priceMin, Double priceMax) {
        if (priceMin == null && priceMax == null) {
            return null;
        } else if (priceMax == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin);
        } else if (priceMin == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax);
        } else {
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price"), priceMin, priceMax);
        }
    }


    public static Specification<Product> hasQuantityMin(ProductFilterRequest filter) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), filter.getQuantityMin()));
    }

    public static Specification<Product> equalsToAnyId(Set<Long> ids) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("id")).value(ids))
        ));
    }



    public static Specification<Product> hasComboId(Long comboId) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Combo> comboJoin = root.join("combos");
            return criteriaBuilder.equal(comboJoin.get("id"), comboId);
        };
    }

    public static Specification<Product> hasStatus(Status status) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<Product> hasStatus(ProductFilterRequest filter) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), filter.getStatus()));
    }
}
