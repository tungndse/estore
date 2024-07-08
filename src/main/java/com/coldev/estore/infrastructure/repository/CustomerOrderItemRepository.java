package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.CustomerOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerOrderItemRepository extends JpaRepository<CustomerOrderItem, Long>,
        JpaSpecificationExecutor<CustomerOrderItem> {

    List<CustomerOrderItem> findByCustomerOrderIdIn(List<Long> customerOrderIds);

}
