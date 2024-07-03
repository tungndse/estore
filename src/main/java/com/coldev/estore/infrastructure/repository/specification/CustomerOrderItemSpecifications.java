package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.domain.entity.CustomerOrderItem;
import org.springframework.data.jpa.domain.Specification;

public class CustomerOrderItemSpecifications {

    public static Specification<CustomerOrderItem> hasCustomerOrderId(Long customerOrderId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("customerOrder").get("id"), customerOrderId));
    }

}
