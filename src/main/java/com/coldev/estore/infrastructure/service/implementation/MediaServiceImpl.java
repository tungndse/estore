package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.domain.entity.Media;
import com.coldev.estore.domain.entity.ProductMedia;
import com.coldev.estore.domain.service.MediaService;
import com.coldev.estore.infrastructure.repository.MediaRepository;
import com.coldev.estore.infrastructure.repository.ProductMediaRepository;
import com.coldev.estore.infrastructure.repository.specification.MediaSpecifications;
import com.coldev.estore.infrastructure.repository.specification.ProductMediaSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final ProductMediaRepository productMediaRepository;

    public MediaServiceImpl(MediaRepository mediaRepository, ProductMediaRepository productMediaRepository) {
        this.mediaRepository = mediaRepository;
        this.productMediaRepository = productMediaRepository;
    }

    @Override
    public Media getMediaById(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Media> getMediaList(Specification<Media> specification) {
        return mediaRepository.findAll(specification);
    }

    @Override
    public ProductMedia createProductMedia(Long productId, Long mediaId) {
        return productMediaRepository.save(
                ProductMedia.builder()
                        .productId(productId)
                        .mediaId(mediaId)
                        .build()
        );
    }

    @Override
    public List<Media> getMediaListByProductId(Long productId) {
        List<ProductMedia> productMediaList =
                productMediaRepository.findAll(ProductMediaSpecifications.hasProductId(productId));

        List<Long> mediaIdList = productMediaList.stream()
                .map(ProductMedia::getMediaId)
                .toList();

        Set<Long> mediaIdSet = new HashSet<>(mediaIdList);

        return this.getMediaList(MediaSpecifications.equalsToAnyId(mediaIdSet));
    }
}
