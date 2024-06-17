package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

}
