package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class CustomerOrderSpecifications {


    public static Specification<CustomerOrder> hasStatus(OrderStatus status) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status));
    }
}
