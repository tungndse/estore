package com.coldev.estore.config.exception.mapper;


import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.product.request.ProductPostDto;
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
}
