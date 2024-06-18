package com.coldev.estore.domain.dto.combo.request;


import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ComboFilterRequest extends FilterRequest {

    private Double discountPercentMin;

    private Double discountPercentMax;

    private BigDecimal discountValueMin;

    private BigDecimal discountValueMax;

    private Status status;

    private Long byProductId;

    private Long byProductCount;

}
