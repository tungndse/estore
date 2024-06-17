package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.common.utility.SortUtils;
import com.coldev.estore.common.utility.SpecificationUtils;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.mapper.ProductMapper;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.Brand;
import com.coldev.estore.domain.entity.Media;
import com.coldev.estore.domain.entity.Product;
import com.coldev.estore.domain.service.MediaService;
import com.coldev.estore.domain.service.ProductService;

import com.coldev.estore.infrastructure.repository.BrandRepository;
import com.coldev.estore.infrastructure.repository.ProductRepository;
import com.coldev.estore.infrastructure.repository.specification.MediaSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final MediaService mediaService;

    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository, BrandRepository brandRepository, MediaService mediaService) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.mediaService = mediaService;
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
            case BASIC -> {}
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
            case TWO_LEVEL_DEPTH -> {}
        }


        return productGetDtoBuilder.build();

    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


    @Override
    public List<ProductGetDto> getProductDtoList(ProductFilterRequest filterRequest,
                                                 ResponseLevel responseLevel) {
        Pageable pageable = SortUtils.getPagination
                (filterRequest.getPageSize(), filterRequest.getPageNo(),
                        filterRequest.getSortOrder(), filterRequest.getSortAttribute());

        Page<Product> productsPage = this.getProductsPage(filterRequest, pageable);

        return productsPage.stream()
                .map(product -> {
                    ProductGetDto.ProductGetDtoBuilder productGetDtoBuilder =
                            productMapper.toProductGetDtoBuilder(product);
                    //TODO Code to find list images and attach to this get dto

                    switch (responseLevel) {
                        case BASIC -> {}
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
                        case TWO_LEVEL_DEPTH -> {}
                    }

                    return productGetDtoBuilder.build();
                })
                .toList();
    }

    @Override
    public Page<Product> getProductsPage(ProductFilterRequest filterRequest, Pageable pageable) {
        Specification<Product> specification =
                Specification.allOf(SpecificationUtils.getSpecifications(filterRequest));

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
}
