package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.mapper.ComboMapper;
import com.coldev.estore.config.exception.mapper.ProductMapper;
import com.coldev.estore.domain.dto.combo.request.ComboPostDto;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.Combo;
import com.coldev.estore.domain.entity.Product;
import com.coldev.estore.domain.entity.ProductCombo;
import com.coldev.estore.domain.service.ComboService;
import com.coldev.estore.domain.service.ProductService;
import com.coldev.estore.infrastructure.repository.ComboRepository;
import com.coldev.estore.infrastructure.repository.ProductComboRepository;
import com.coldev.estore.infrastructure.repository.specification.ProductComboSpecifications;
import com.coldev.estore.infrastructure.repository.specification.ProductSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Log4j2
public class ComboServiceImpl implements ComboService {

    private final ComboRepository comboRepository;
    private final ProductComboRepository productComboRepository;
    private final ProductService productService;
    private final ComboMapper comboMapper;
    private final ProductMapper productMapper;

    public ComboServiceImpl(ComboRepository comboRepository, ProductComboRepository productComboRepository,
                            @Lazy ProductService productService,
                            ComboMapper comboMapper, ProductMapper productMapper) {
        this.comboRepository = comboRepository;
        this.productComboRepository = productComboRepository;
        this.productService = productService;
        this.comboMapper = comboMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Combo createNewCombo(ComboPostDto payload) {

        Combo.ComboBuilder newComboBuilder = comboMapper.toNewComboBuilder(payload);

        log.info(payload.getProductIds());

        Combo savedCombo = comboRepository.save(newComboBuilder.build());

        if (payload.getProductIds() != null && !payload.getProductIds().isEmpty()) {
            //TODO Experimental
            Set<Long> productIds = payload.getProductIds();
            List<Product> products = productService
                    .getProductsList(ProductSpecifications.equalsToAnyId(productIds));

            log.info(products);

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
    public ComboGetDto getComboById(Long id, ResponseLevel responseLevel) {
        Combo combo = comboRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.COMBO));

        switch (responseLevel) {
            case BASIC -> {}

            case ONE_INNER_LIST -> {
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

            case TWO_INNER_LISTS -> {}

            default -> {
                return comboMapper.toComboGetDto(combo);
            }
        }

        return null;

    }
}
