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


    default Product toNewProductEntity(ProductPostDto payload) {
        if (payload == null) return null;

        return Product.builder()
                .name(payload.getName())
                .description(payload.getDescription())
                .category(payload.getCategory())
                .status(Status.ACTIVE)
                .imageUrl(payload.getMainMediaUrl())
                .quantity(payload.getQuantity())
                .price(payload.getPrice())
                .build();
    }

    default ProductGetDto.ProductGetDtoBuilder toProductGetDtoBuilder(Product product) {
        if (product == null) return null;

        return ProductGetDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .status(product.getStatus())
                .imageUrl(product.getImageUrl())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt());
    }
}
