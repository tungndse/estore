package com.coldev.estore.domain.dto.product.response;


import com.coldev.estore.common.enumerate.Category;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.common.serializer.MoneyBigDecimalSerializer;
import com.coldev.estore.domain.dto.combo.response.ComboGetDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductGetDto implements Serializable {

    private Long id;
    private String name;
    private String description;

    @JsonSerialize(using = MoneyBigDecimalSerializer.class)
    private BigDecimal price;

    @JsonProperty("brand")
    private String brandName;

    @JsonProperty("image_url")
    private String imageUrl;

    private Long quantity;

    @JsonProperty("created_at")
    private Date createdAt;

    private Status status;

    private Category category;

    @JsonProperty("sub_media_urls")
    private List<String> subMediaUrls;

    @JsonProperty("combo_list")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ComboGetDto> comboGetDtoList;

}
