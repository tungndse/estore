package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>,
        JpaSpecificationExecutor<CustomerOrder> {


}
