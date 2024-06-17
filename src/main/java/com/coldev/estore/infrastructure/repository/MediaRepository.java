package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MediaRepository extends JpaRepository<Media, Long>,
        JpaSpecificationExecutor<Media> {
    Media getMediaByKey(String key);
}