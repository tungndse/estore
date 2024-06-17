package com.coldev.estore.domain.dto.product.request;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductPostDto {

    @JsonProperty("brand_id")
    @NotBlank(message = ConstantDictionary.BRAND + ": " + MessageDictionary.NOT_NULL)
    private Long brandId;

    @NotBlank(message = ConstantDictionary.NAME + ": " + MessageDictionary.NOT_NULL)
    private String name;

    @NotBlank(message = ConstantDictionary.DESCRIPTION + ": " + MessageDictionary.NOT_NULL)
    private String description;

    private Long quantity;

    private BigDecimal price;

    @Builder.Default
    private Status status = Status.ACTIVE;

    private Category category;

    @JsonProperty("main_media_id")
    @Nullable
    private Long mainMediaId;

    @JsonProperty("sub_media_ids")
    @Nullable
    @JsonPropertyDescription("Can be left null")
    private Set<Long> subMediaIds;
}