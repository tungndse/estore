package com.coldev.estore.domain.dto.customerorder.request;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.domain.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderFilterRequest extends FilterRequest implements Serializable {
    private OrderStatus status;

}
