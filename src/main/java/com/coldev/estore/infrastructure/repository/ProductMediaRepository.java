package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, Long>,
        JpaSpecificationExecutor<ProductMedia> {
}
