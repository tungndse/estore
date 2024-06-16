package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {


}
