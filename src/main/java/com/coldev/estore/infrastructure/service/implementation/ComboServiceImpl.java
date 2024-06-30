package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.common.utility.SortUtils;
import com.coldev.estore.common.utility.SpecificationUtils;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.mapper.ComboMapper;
import com.coldev.estore.config.exception.mapper.ProductMapper;
import com.coldev.estore.domain.dto.combo.request.ComboFilterRequest;
import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.*;
import com.coldev.estore.domain.service.ComboService;
import com.coldev.estore.domain.service.MediaService;
import com.coldev.estore.domain.service.ProductService;
import com.coldev.estore.infrastructure.repository.ComboRepository;
import com.coldev.estore.infrastructure.repository.ProductComboRepository;
import com.coldev.estore.infrastructure.repository.specification.*;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
@Log4j2
public class ComboServiceImpl implements ComboService {

    private final ComboRepository comboRepository;
    private final ProductComboRepository productComboRepository;
    private final ProductService productService;
    private final MediaService mediaService;
    private final ComboMapper comboMapper;
    private final ProductMapper productMapper;

    public ComboServiceImpl(ComboRepository comboRepository, ProductComboRepository productComboRepository,
                            @Lazy ProductService productService, MediaService mediaService,
                            ComboMapper comboMapper, ProductMapper productMapper) {
        this.comboRepository = comboRepository;
        this.productComboRepository = productComboRepository;
        this.productService = productService;
        this.mediaService = mediaService;
        this.comboMapper = comboMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Combo createNewCombo(ComboPostDto payload) {

        Combo.ComboBuilder newComboBuilder = comboMapper.toNewComboBuilder(payload);

        if (payload.getMainMediaId() != null) {
            Media media = mediaService.getMediaById(payload.getMainMediaId());
            newComboBuilder.media(media);
        }

        log.info(payload.getProductIds());

        Combo savedCombo = comboRepository.save(newComboBuilder.build());

        if (payload.getProductIds() != null && !payload.getProductIds().isEmpty()) {

            Set<Long> productIdSet = payload.getProductIds();
            List<Product> products = productService
                    .getProductList(ProductSpecifications.equalsToAnyId(productIdSet));

            List<Long> productIds = products.stream()
                    .map(Product::getId).toList();

            for (Long productId : productIds) {
                productComboRepository.save(
                        ProductCombo.builder()
                                .productId(productId)
                                .comboId(savedCombo.getId())
                                .build()
                );
            }
        }

        return savedCombo;
    }

    @Override
    public ComboGetDto getComboDtoById(Long id, ResponseLevel responseLevel) {
        Combo combo = comboRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.COMBO));

        switch (responseLevel) {
            case BASIC -> {
            }

            case ONE_LEVEL_DEPTH -> {
                log.info("ONE INNER LIST");
                List<ProductCombo> productComboList = productComboRepository
                        .findAll(ProductComboSpecifications.hasComboId(combo.getId()));

                List<Product> products = productComboList.stream()
                        .map(productCombo -> productService.getProductById(productCombo.getProductId())
                        ).filter(Objects::nonNull)
                        .toList();

                List<ProductGetDto> productGetDtoList = products.stream().map(
                        product -> productMapper.toProductGetDtoBuilder(product).build()
                ).toList();

                ComboGetDto.ComboGetDtoBuilder comboGetDtoBuilder = comboMapper
                        .toComboGetDtoBuilder(combo)
                        .productGetDtoList(productGetDtoList);

                return comboGetDtoBuilder.build();
            }

            case TWO_LEVEL_DEPTH -> {
            }

            default -> {
                return comboMapper.toComboGetDto(combo);
            }
        }

        return null;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Combo updateCombo(Long id, ComboPostDto comboPostDto) {

        Combo combo = comboRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.COMBO));

        Combo updatedCombo = this.mergeFromUpdate(combo, comboPostDto);

        if (comboPostDto.getProductIds() != null && !comboPostDto.getProductIds().isEmpty()) {
            Set<Long> updatedProductIdSet = comboPostDto.getProductIds();
            Set<Long> originalProductIdSet = new HashSet<>(
                    productComboRepository
                            .findAll(ProductComboSpecifications.hasComboId(combo.getId()))
                            .stream()
                            .map(ProductCombo::getProductId)
                            .toList()
            );

            log.info("Original Product IDs Set: " + originalProductIdSet);
            log.info("New Product IDs Set: " + updatedProductIdSet);

            Set<Long> intersection = Sets.intersection(updatedProductIdSet, originalProductIdSet);
            log.info("Intersection Product IDs Set: " + intersection);

            log.info("---- RESULT AS BELOW ---- ");
            Set<Long> comparedToOriginalSet = Sets.symmetricDifference(originalProductIdSet, intersection);
            log.info("Product IDs Set [to be deleted]: " + comparedToOriginalSet);

            Set<Long> comparedToUpdatedSet = Sets.symmetricDifference(updatedProductIdSet, intersection);
            log.info("Product IDs Set [to be added]: " + comparedToUpdatedSet);

            productComboRepository.delete(
                    ProductComboSpecifications.hasComboId(combo.getId())
                            .and(ProductComboSpecifications.equalsToAnyProductId(comparedToOriginalSet))
            );

            for (Long productId : comparedToUpdatedSet) {
                productComboRepository.save(
                        ProductCombo.builder()
                                .productId(productId)
                                .comboId(combo.getId())
                                .build()
                );
            }
        }

        log.info("---- COMPLETED PRODUCT COMBO OPERATION ---");

        return comboRepository.save(updatedCombo);
    }

    @Override
    public List<Combo> getComboListByProductId(Long productId) {
        List<ProductCombo> productComboList =
                productComboRepository.findAll(ProductComboSpecifications.hasProductId(productId));

        List<Long> comboIdList = productComboList.stream()
                .map(ProductCombo::getComboId)
                .toList();

        Set<Long> comboIdSet = new HashSet<>(comboIdList);

        return comboRepository.findAll(ComboSpecifications.equalsToAnyId(comboIdSet));
    }

    @Override
    public List<ComboGetDto> getComboDtoList(ComboFilterRequest comboFilterRequest,
                                             ResponseLevel responseLevel) {
        Pageable pageable = SortUtils.getPagination(
                comboFilterRequest.getPageSize(), comboFilterRequest.getPageNo(),
                comboFilterRequest.getSortOrder(), comboFilterRequest.getSortAttribute()
        );

        Page<Combo> comboPage = this.getComboPage(comboFilterRequest, pageable);

        return comboPage.stream()
                .map(combo -> {

                    ComboGetDto.ComboGetDtoBuilder comboGetDtoBuilder =
                            comboMapper.toComboGetDtoBuilder(combo);

                    List<Product> products = productService.getProductList(
                            ProductSpecifications.hasComboId(combo.getId())
                    );

                    BigDecimal productsTotalAfterDeduction =
                            this.calculateComboTotalAfterDeduction(combo, products);

                    comboGetDtoBuilder.productsTotal(productsTotalAfterDeduction);

                    switch (responseLevel) {
                        case BASIC -> {

                        }
                        case ONE_LEVEL_DEPTH -> {
                            //Not yet
                        }

                        case TWO_LEVEL_DEPTH -> {
                            // not yet
                        }
                    }

                    return comboGetDtoBuilder.build();
                })
                .toList();

    }

    private BigDecimal calculateComboTotalAfterDeduction(Combo combo, List<Product> products) {
        BigDecimal productsTotal = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal deductionByPercent = combo.getDiscountPercentage() > 0 ?
                productsTotal.multiply(BigDecimal.valueOf(combo.getDiscountPercentage()))
                :
                BigDecimal.ZERO;

        BigDecimal deductionByValue = combo.getDiscountValue();

        BigDecimal productsTotalAfterDeduction = productsTotal
                .subtract(deductionByPercent)
                .subtract(deductionByValue);

        if (productsTotalAfterDeduction.compareTo(BigDecimal.ZERO) <= 0) {
            productsTotalAfterDeduction = BigDecimal.ZERO;
        }

        return productsTotalAfterDeduction;
    }

    @Override
    public Page<Combo> getComboPage(ComboFilterRequest comboFilterRequest, Pageable pageable) {
        Specification<Combo> specification =
                Specification.allOf(SpecificationUtils.getSpecifications(comboFilterRequest));

        return comboRepository.findAll(specification, pageable);
    }

    private Combo mergeFromUpdate(Combo combo, ComboPostDto comboPostDto) {
        Combo.ComboBuilder comboBuilder = Combo.builder()
                .id(combo.getId())
                .status(comboPostDto.getStatus() != null ? comboPostDto.getStatus() : combo.getStatus())
                .name(comboPostDto.getName() != null ? comboPostDto.getName() : combo.getName())
                .description(comboPostDto.getDescription() != null ? comboPostDto.getDescription() : combo.getDescription())
                .discountPercentage(comboPostDto.getDiscountPercentage() != null ?
                        comboPostDto.getDiscountPercentage() : combo.getDiscountPercentage())
                .discountValue(comboPostDto.getDiscountValue() != null ?
                        comboPostDto.getDiscountValue() : combo.getDiscountValue());

        if (comboPostDto.getMainMediaId() != null) {
            Media comboMainMedia = mediaService.getMediaById(comboPostDto.getMainMediaId());
            if (comboMainMedia != null) {
                comboBuilder.media(comboMainMedia);
            }
        }

        return comboBuilder.build();
    }
}
