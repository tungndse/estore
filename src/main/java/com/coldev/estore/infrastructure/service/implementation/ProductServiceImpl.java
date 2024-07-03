package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.common.utility.SortUtils;
import com.coldev.estore.common.utility.SpecificationUtils;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.general.ItemUnavailableException;
import com.coldev.estore.config.exception.mapper.ComboMapper;
import com.coldev.estore.config.exception.mapper.ProductMapper;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.request.ProductPutDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.*;
import com.coldev.estore.domain.service.ComboService;
import com.coldev.estore.domain.service.MediaService;
import com.coldev.estore.domain.service.ProductService;

import com.coldev.estore.infrastructure.repository.BrandRepository;
import com.coldev.estore.infrastructure.repository.ProductMediaRepository;
import com.coldev.estore.infrastructure.repository.ProductRepository;
import com.coldev.estore.infrastructure.repository.specification.MediaSpecifications;
import com.coldev.estore.infrastructure.repository.specification.ProductMediaSpecifications;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {

    private final ComboMapper comboMapper;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final MediaService mediaService;
    private final ComboService comboService;
    private final ProductMediaRepository productMediaRepository;

    public ProductServiceImpl(ComboMapper comboMapper, ProductMapper productMapper, ProductRepository productRepository, BrandRepository brandRepository, MediaService mediaService, ComboService comboService, ProductMediaRepository productMediaRepository) {
        this.comboMapper = comboMapper;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.mediaService = mediaService;
        this.comboService = comboService;
        this.productMediaRepository = productMediaRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product createNewProduct(ProductPostDto productPostDto) throws IOException {
        Product.ProductBuilder newProductBuilder = productMapper.toNewProductBuilder(productPostDto);

        Brand brand = this.getBrandById(productPostDto.getBrandId());
        if (brand == null) throw new ItemNotFoundException(null, ConstantDictionary.BRAND);
        newProductBuilder.brand(brand);

        if (productPostDto.getMainMediaId() != null) {
            Media productMainMedia = mediaService.getMediaById(productPostDto.getMainMediaId());
            if (productMainMedia != null) {
                newProductBuilder.media(productMainMedia);
            }
        }

        Product savedProduct = productRepository.save(newProductBuilder.build());

        if (productPostDto.getSubMediaIds() != null && !productPostDto.getSubMediaIds().isEmpty()) {
            Set<Long> subMediaIdSet = productPostDto.getSubMediaIds();

            List<Media> subMediaList = mediaService
                    .getMediaList(MediaSpecifications.equalsToAnyId(subMediaIdSet));

            List<Long> subMediaIds = subMediaList.stream()
                    .map(Media::getId).toList();

            for (Long subMediaId : subMediaIds) {
                mediaService.createProductMedia(savedProduct.getId(), subMediaId);
            }

        }

        return savedProduct;

    }

    @Override
    public ProductGetDto getProductDtoById(Long id, ResponseLevel responseLevel) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.PRODUCT));

        ProductGetDto.ProductGetDtoBuilder productGetDtoBuilder =
                productMapper.toProductGetDtoBuilder(product);

        // TODO Code to list sub-media urls for getDTO
        //
        // Mapper should not call services
        switch (responseLevel) {
            case BASIC -> {
            }
            case ONE_LEVEL_DEPTH -> {

                //Media
                List<Media> mediaList =
                        mediaService.getMediaListByProductId(product.getId());

                if (mediaList != null && !mediaList.isEmpty()) {
                    List<String> subMediaUrls = mediaList.stream()
                            .map(Media::getUrl)
                            .toList();
                    productGetDtoBuilder.subMediaUrls(subMediaUrls);
                }

                //Combo
                List<Combo> comboList = comboService.getComboListByProductId(product.getId());
                if (comboList != null && !comboList.isEmpty()) {
                    List<ComboGetDto> comboGetDtoList = comboList.stream()
                            .map(comboMapper::toComboGetDto)
                            .toList();

                    productGetDtoBuilder.comboGetDtoList(comboGetDtoList);
                }

            }
            case TWO_LEVEL_DEPTH -> {
            }
        }


        return productGetDtoBuilder.build();

    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product getProductByIdWithNullCheck(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.PRODUCT));
    }

    @Override
    public Product getProductByIdWithAvailabilityCheck(Long id) {
        Product product = this.getProductByIdWithNullCheck(id);

        if (product.getQuantity() < 1 || product.getStatus() != Status.ACTIVE)
            throw new ItemUnavailableException(id, ConstantDictionary.PRODUCT);

        return product;
    }

    @Override
    public boolean checkProductAvailability(Product product) {
        return product.getQuantity() >= 1 && product.getStatus() == Status.ACTIVE;
    }


    @Override
    public List<ProductGetDto> getProductDtoList(ProductFilterRequest filterRequest,
                                                 ResponseLevel responseLevel) {
        Pageable pageable = SortUtils.getPagination(
                filterRequest.getPageSize(), filterRequest.getPageNo(),
                filterRequest.getSortOrder(), filterRequest.getSortAttribute());

        Page<Product> productPage = this.getProductPage(filterRequest, pageable);

        return productPage.stream()
                .map(product -> {
                    ProductGetDto.ProductGetDtoBuilder productGetDtoBuilder =
                            productMapper.toProductGetDtoBuilder(product);

                    switch (responseLevel) {
                        case BASIC -> {
                        }
                        case ONE_LEVEL_DEPTH -> {
                            List<Media> mediaList =
                                    mediaService.getMediaListByProductId(product.getId());

                            if (mediaList != null && !mediaList.isEmpty()) {
                                List<String> subMediaUrls = mediaList.stream()
                                        .map(Media::getUrl)
                                        .toList();
                                productGetDtoBuilder.subMediaUrls(subMediaUrls);
                            }
                        }
                        case TWO_LEVEL_DEPTH -> {
                            // Optional Later
                        }
                    }

                    return productGetDtoBuilder.build();
                })
                .toList();
    }

    @Override
    public Page<Product> getProductPage(ProductFilterRequest filterRequest, Pageable pageable) {
        Specification<Product> specification =
                Specification.allOf(SpecificationUtils.getSpecifications(filterRequest))
                //.and((ProductSpecifications.hasStatus(Status.ACTIVE)))
                ;

        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> getProductList(Specification<Product> specification) {
        return productRepository.findAll(specification);
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public Long deleteProductById(Long id) {
        Product product = this.getProductById(id);
        if (product == null) return null;
        else product.setStatus(Status.DELETED);
        return productRepository.save(product).getId();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product updateProduct(ProductPutDto productPutDto) {

        Product productToBeUpdated = this.getProductById(productPutDto.getId());

        if (productToBeUpdated == null)
            throw new ItemNotFoundException(productPutDto.getId(), ConstantDictionary.PRODUCT);

        Product.ProductBuilder updatingProductBuilder =
                productMapper.updateNonNullFields(productToBeUpdated, productPutDto);

        if (productPutDto.getBrandId() != null) {
            Brand brand = this.getBrandById(productPutDto.getBrandId());
            if (brand == null) throw new ItemNotFoundException(productPutDto.getBrandId(), ConstantDictionary.BRAND);
            updatingProductBuilder.brand(brand);
        }

        if (productPutDto.getMainMediaId() != null) {
            Media productMainMedia = mediaService.getMediaById(productPutDto.getMainMediaId());
            if (productMainMedia != null) {
                updatingProductBuilder.media(productMainMedia);
            }
        }

        if (productPutDto.getSubMediaIds() != null && !productPutDto.getSubMediaIds().isEmpty()) {
            /*Set<Long> updatingMediaIdSet = productPutDto.getSubMediaIds();
            Set<Long> originalMediaIdSet = new HashSet<>(
                    productMediaRepository
                            .findAll(ProductMediaSpecifications.hasProductId(productToBeUpdated.getId()))
                            .stream()
                            .map(ProductMedia::getProductId)
                            .toList()
            );

            log.info("Original Media IDs Set: " + originalMediaIdSet);
            log.info("New Media IDs Set: " + updatingMediaIdSet);

            Set<Long> intersection = Sets.intersection(updatingMediaIdSet, originalMediaIdSet);
            log.info("Intersection Media IDs Set: " + intersection);

            log.info("---- RESULT AS BELOW ---- ");
            Set<Long> comparedToOriginalSet = Sets.symmetricDifference(originalMediaIdSet, intersection);
            log.info("Media IDs Set [to be deleted]: " + comparedToOriginalSet);

            Set<Long> comparedToUpdatedSet = Sets.symmetricDifference(updatingMediaIdSet, intersection);
            log.info("Media IDs Set [to be added]: " + comparedToUpdatedSet);

            productMediaRepository.delete(
                    ProductMediaSpecifications.hasProductId(productToBeUpdated.getId())
                            .and(ProductMediaSpecifications.equalsToAnyMediaId(comparedToOriginalSet))
            );

            for (Long mediaId : comparedToUpdatedSet) {
                productMediaRepository.save(
                        ProductMedia.builder()
                                .productId(productToBeUpdated.getId())
                                .mediaId(mediaId)
                                .build()
                );
            }*/
            this.handleMediaIds(productToBeUpdated, productPutDto.getSubMediaIds());
        }

        return productRepository.save(updatingProductBuilder.build());

    }

    @Override
    public void updateProductQuantity(Product product, Long orderedQuantity) {

        if (orderedQuantity > product.getQuantity())
            throw new ItemUnavailableException(
                    product.getId(),
                    ConstantDictionary.PRODUCT,
                    "Name="+product.getName() +
                            "; ProductStock=" + product.getQuantity() +
                            "; OrderedQuantity=" + orderedQuantity
            );

        product.setQuantity(product.getQuantity() - orderedQuantity);

        productRepository.save(product);
    }

    private void handleMediaIds(Product productToBeUpdated, Set<Long> updatingMediaIdSet) {

        Set<Long> originalMediaIdSet = productMediaRepository
                .findAll(ProductMediaSpecifications.hasProductId(productToBeUpdated.getId()))
                .stream()
                .map(ProductMedia::getMediaId)
                .collect(Collectors.toSet());

        Set<Long> comparedToOriginalSet = Sets.symmetricDifference(originalMediaIdSet, updatingMediaIdSet);

        productMediaRepository.delete(
                ProductMediaSpecifications.hasProductId(productToBeUpdated.getId())
                        .and(ProductMediaSpecifications.equalsToAnyMediaId(comparedToOriginalSet))
        );

        for (Long mediaId : updatingMediaIdSet) {
            if (!originalMediaIdSet.contains(mediaId)) {
                productMediaRepository.save(ProductMedia.builder()
                        .productId(productToBeUpdated.getId())
                        .mediaId(mediaId)
                        .build());
            }
        }
    }
}
