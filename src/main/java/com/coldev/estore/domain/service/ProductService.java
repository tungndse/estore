package com.coldev.estore.domain.service;

import com.auth0.jwt.interfaces.Payload;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {


    Product createNewProduct(ProductPostDto payload) throws IOException;

    ProductGetDto getProductById(Long id);

    List<ProductGetDto> getProductDtoList(ProductFilterRequest filterRequest);
}
