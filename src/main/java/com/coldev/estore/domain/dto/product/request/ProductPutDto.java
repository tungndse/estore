package com.coldev.estore.domain.dto.product.request;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductPutDto {

    public static final String NOT_EMPTY_OR_WHITESPACE_REGEX = "^\\S.*$";

    @NotBlank(message = ConstantDictionary.ID + ": " + MessageDictionary.NOT_NULL)
    private Long id;

    @JsonProperty("brand_id")
    private Long brandId;

    @Pattern(regexp = NOT_EMPTY_OR_WHITESPACE_REGEX,
            message = ConstantDictionary.NAME + ": " + MessageDictionary.NOT_EMPTY)
    private String name;

    @Pattern(regexp = NOT_EMPTY_OR_WHITESPACE_REGEX,
            message = ConstantDictionary.DESCRIPTION + ": " + MessageDictionary.NOT_EMPTY)
    private String description;

    @Min(value = 0, message = ConstantDictionary.QUANTITY + ": " +
            MessageDictionary.MUST_BE_POSITIVE_INT)
    private Long quantity;

    @DecimalMin(value = "0.0", message = ConstantDictionary.PRICE + ": " +
            MessageDictionary.MUST_BE_POSITIVE_NUMBER)
    private BigDecimal price;

    private Status status;

    private Category category;

    @JsonProperty("main_media_id")
    @Nullable
    private Long mainMediaId;

    @JsonProperty("sub_media_ids")
    @Nullable
    @JsonPropertyDescription("Can be left null")
    private Set<Long> subMediaIds;

}
