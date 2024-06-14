package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.utility.SortUtils;
import com.coldev.estore.common.utility.SpecificationUtils;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.mapper.ProductMapper;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.Product;
import com.coldev.estore.domain.service.ProductService;

import com.coldev.estore.infrastructure.repository.ProductRepository;
import com.coldev.estore.infrastructure.repository.specification.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product createNewProduct(ProductPostDto payload) throws IOException {
        Product newProduct = productMapper.toNewProductEntity(payload);
        return productRepository.save(newProduct);
    }

    @Override
    public ProductGetDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.PRODUCT));

        ProductGetDto.ProductGetDtoBuilder productGetDtoBuilder =
                productMapper.toProductGetDtoBuilder(product);

        // TODO Code to list sub-media urls for getDTO
        //
        // Mapper should not call services

        return productGetDtoBuilder.build();

    }

    @Override
    public List<ProductGetDto> getProductDtoList(ProductFilterRequest filterRequest) {
        Pageable pageable = SortUtils.getPagination
                (filterRequest.getPageSize(), filterRequest.getPageNo(),
                        filterRequest.getSortOrder(), filterRequest.getSortAttribute());

        Page<Product> productsPage = this.getProductsPage(filterRequest, pageable);

        return productsPage.stream()
                .map(product -> {
                    ProductGetDto.ProductGetDtoBuilder productGetDtoBuilder =
                            productMapper.toProductGetDtoBuilder(product);
                    //TODO Code to find list images and attach to this get dto

                    return productGetDtoBuilder.build();
                })
                .toList();
    }

    public Page<Product> getProductsPage(ProductFilterRequest filterRequest, Pageable pageable) {
        Specification<Product> specification =
                Specification.allOf(SpecificationUtils.getSpecifications(filterRequest));

        return productRepository.findAll(specification, pageable);
    }
}
