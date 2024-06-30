package com.coldev.estore.config.exception.mapper;


import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
import com.coldev.estore.domain.dto.product.request.ProductPutDto;
import com.coldev.estore.domain.dto.product.response.ProductGetDto;
import com.coldev.estore.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
        //, uses = {AddressMapper.class, PasswordValidator.class, FormMapper.class}
)
public interface ProductMapper {

    default Product toNewProduct(ProductPostDto payload) {
        if (payload == null) return null;

        return Product.builder()
                .name(payload.getName())
                .description(payload.getDescription())
                .category(payload.getCategory())
                .status(Status.ACTIVE)
                .quantity(payload.getQuantity())
                .price(payload.getPrice())
                .build();
    }

    default Product.ProductBuilder toNewProductBuilder(ProductPostDto payload) {
        if (payload == null) return null;

        return Product.builder()
                .name(payload.getName())
                .description(payload.getDescription())
                .category(payload.getCategory())
                .status(Status.ACTIVE)
                .quantity(payload.getQuantity())
                .price(payload.getPrice());
    }

    default ProductGetDto.ProductGetDtoBuilder toProductGetDtoBuilder(Product product) {
        if (product == null) return null;

        ProductGetDto.ProductGetDtoBuilder productGetDtoBuilder =
                ProductGetDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .category(product.getCategory())
                        .status(product.getStatus())
                        .quantity(product.getQuantity())
                        .price(product.getPrice())
                        .createdAt(product.getCreatedAt());

        if (product.getBrand() != null) {
            productGetDtoBuilder.brandName(product.getBrand().getName());
        }

        if (product.getMedia() != null) {
            productGetDtoBuilder.imageUrl(product.getMedia().getUrl());
        }

        return productGetDtoBuilder;
    }

    default Product.ProductBuilder updateNonNullFields
            (Product product, ProductPutDto productPutDto) {
        if (product == null || productPutDto == null) {
            return null;
        }

        Product.ProductBuilder productBuilder = Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .status(product.getStatus())
                .category(product.getCategory())

                .brand(product.getBrand())
                .media(product.getMedia())
                .subMediaList(product.getSubMediaList());

        if (productPutDto.getName() != null && !productPutDto.getName().trim().isEmpty()) {
            productBuilder.name(productPutDto.getName());
        }
        if (productPutDto.getDescription() != null && !productPutDto.getDescription().trim().isEmpty()) {
            productBuilder.description(productPutDto.getDescription());
        }
        if (productPutDto.getQuantity() != null) {
            productBuilder.quantity(productPutDto.getQuantity());
        }
        if (productPutDto.getPrice() != null) {
            productBuilder.price(productPutDto.getPrice());
        }
        if (productPutDto.getStatus() != null) {
            productBuilder.status(productPutDto.getStatus());
        }
        if (productPutDto.getCategory() != null) {
            productBuilder.category(productPutDto.getCategory());
        }

        return productBuilder;
    }
}
