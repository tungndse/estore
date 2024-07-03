package com.coldev.estore.domain.dto.customerorder.item.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboItemPostDto {

    private Long id;
    private Long quantity;

}
