package com.coldev.estore.domain;

import com.coldev.estore.common.enumerate.SortType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest implements Serializable {

    @JsonProperty("page_no")
    private int pageNo;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("sort_attribute")
    private String sortAttribute;

    @JsonProperty("sort_order")
    private SortType sortOrder;

    @JsonProperty("search_key")
    private String searchKey;

    @JsonProperty("description_contains")
    private String descriptionContains;

}
