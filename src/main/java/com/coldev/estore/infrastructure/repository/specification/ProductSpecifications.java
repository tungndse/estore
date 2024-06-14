package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> hasCategory(ProductFilterRequest filterRequest) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), filterRequest.getCategory()));
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


    public static Specification<Product> hasQuantityMin(ProductFilterRequest filter) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), filter.getQuantityMin()));
    }
}
