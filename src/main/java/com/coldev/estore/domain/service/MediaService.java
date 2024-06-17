package com.coldev.estore.domain.service;

import com.coldev.estore.domain.entity.Media;
import com.coldev.estore.domain.entity.ProductMedia;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface MediaService {
    Media getMediaById(Long id);

    List<Media> getMediaList(Specification<Media> specification);

    ProductMedia createProductMedia(Long productId, Long mediaId);

    List<Media> getMediaListByProductId(Long productId);
}
