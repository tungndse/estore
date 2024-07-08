package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.Year;
import java.util.Calendar;

public class CustomerOrderSpecifications {


    public static Specification<CustomerOrder> hasYear(int year) {
        return (root, query, cb) -> cb.equal(cb.function("year", Integer.class, root.get("orderDate")), year);
    }

    public static Specification<CustomerOrder> hasMonth(int month) {
        return (root, query, cb) -> cb.equal(cb.function("month", Integer.class, root.get("orderDate")), month);
    }

    public static Specification<CustomerOrder> hasCurrentYear() {
        int currentYear = Year.now().getValue();
        return hasYear(currentYear);
    }

    public static Specification<CustomerOrder> hasStatus(OrderStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
