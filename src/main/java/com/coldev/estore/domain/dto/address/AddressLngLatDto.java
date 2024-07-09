package com.coldev.estore.domain.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressLngLatDto {
    private Double latitude;
    private Double longitude;
}
