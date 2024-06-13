package com.coldev.estore.infrastructure.repository;

import com.coldev.estore.domain.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Media getMediaByKey(String key);
}