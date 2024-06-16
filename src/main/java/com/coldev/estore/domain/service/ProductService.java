package com.coldev.estore.domain.service;

import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

public interface ProductService {


    Product createNewProduct(ProductPostDto payload) throws IOException;

    ProductGetDto getProductDtoById(Long id);

    Product getProductById(Long id);

    List<ProductGetDto> getProductDtoList(ProductFilterRequest filterRequest);

    Page<Product> getProductsPage(ProductFilterRequest filterRequest, Pageable pageable);

    List<Product> getProductsList(Specification<Product> specification);
}
