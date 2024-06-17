package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.mapper.ComboMapper;
import com.coldev.estore.config.exception.mapper.ProductMapper;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
            //TODO Experimental
            Set<Long> productIdSet = payload.getProductIds();
            log.info("start stream product");
            List<Product> products = productService
                    .getProductList(ProductSpecifications.equalsToAnyId(productIdSet));
            log.info("finished streaming");

            //log.info(products);

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
            Set<Long> productIds = comboPostDto.getProductIds();
            // TODO algorithm to update ProductCombo for the best efficiency

            // TODO then save the updatedProductCombo map to database here
        }

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
