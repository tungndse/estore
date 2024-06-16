package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.ProductCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductComboRepository extends JpaRepository<ProductCombo, Long>,
        JpaSpecificationExecutor<ProductCombo> {

    //List<ProductCombo> findProductComboByComboId(Long comboId);

}
