package com.coldev.estore.domain.service;

import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.request.ProductPutDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.Brand;
import com.coldev.estore.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

public interface ProductService {


    Product createNewProduct(ProductPostDto payload) throws IOException;

    ProductGetDto getProductDtoById(Long id, ResponseLevel responseLevel);

    Product getProductById(Long id);

    Product getProductByIdWithNullCheck(Long id);

    Product getProductByIdWithAvailabilityCheck(Long id);

    boolean checkProductAvailability(Product product);

    List<ProductGetDto> getProductDtoList(ProductFilterRequest filterRequest, ResponseLevel responseLevel);

    Page<Product> getProductPage(ProductFilterRequest filterRequest, Pageable pageable);

    List<Product> getProductList(Specification<Product> specification);

    Brand getBrandById(Long id);

    Long deleteProductById(Long id);

    Product updateProduct(ProductPutDto productPutDto);

    void updateProductQuantity(Product product, Long newQuantity);
}
