package com.coldev.estore.application.controller;


import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.AccountRole;
import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.SortType;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.config.exception.general.BadRequestException;
import com.coldev.estore.config.exception.general.DataNotFoundException;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.service.AuthService;
import com.coldev.estore.domain.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Log4j2
public class ProductController {

    final
    ProductService productService;

    private final AuthService authService;

    public ProductController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductPostDto payload)
            throws IOException, BadRequestException, DataNotFoundException {
        //Check admin
        AccountRole role = authService.retrieveTokenizedAccountRole();
        if (role != AccountRole.ADMIN) throw new BadRequestException(MessageDictionary.ACCESS_DENIED);

        ProductGetDto productResponse = productService.getProductById(productService.createNewProduct(payload).getId());

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.ACTION_SUCCESS)
                        .data(productResponse)
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<ResponseObject<List<ProductGetDto>>> getAllProducts
            (@RequestParam(name = "page", defaultValue = "0") int page,
             @RequestParam(name = "size", defaultValue = "10") int size,
             @RequestParam(name = "category") Category category,
             @RequestParam(name = "search_key", required = false) String searchKey,
             @RequestParam(name = "sort_by", required = false) String sortBy,
             @RequestParam(name = "sort_type", required = false) SortType sortType,
             @RequestParam(name = "price_min", required = false) Double priceMin,
             @RequestParam(name = "price_max", required = false) Double priceMax,
             @RequestParam(name = "status", required = false) Status productStatus,
             @RequestParam(name = "quantity_min", required = false) Long quantityMin
            ) throws IOException, BadRequestException {


        AccountRole role = authService.retrieveTokenizedAccountRole();

        ProductFilterRequest filterRequest = ProductFilterRequest.builder()
                .pageNo(page).pageSize(size)
                .sortOrder(sortType).sortAttribute(sortBy)
                .category(category)
                .searchKey(searchKey)
                .priceMin(priceMin).priceMax(priceMax)
                .status(productStatus).quantityMin(quantityMin)
                .build();

        List<ProductGetDto> productGetDtoList = productService.getProductDtoList
                (filterRequest);
        ResponseObject.ResponseObjectBuilder<List<ProductGetDto>> responseBuilder =
                ResponseObject.builder();

        if (!productGetDtoList.isEmpty()) {
            ResponseObject<List<ProductGetDto>> response = responseBuilder
                    .message(MessageDictionary.DATA_FOUND)
                    .data(productGetDtoList)
                    .totalItems(productGetDtoList.size()).build();

            return ResponseEntity.ok(response);
        } else {
            ResponseObject<List<ProductGetDto>> response =responseBuilder.message(MessageDictionary.DATA_NOT_FOUND)
                    .totalItems(0).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


    }

}