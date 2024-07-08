package com.coldev.estore.domain.dto.dashboard.response;

import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dashboard implements Serializable {

    private Year year;

    private Month month;

    @JsonProperty("total_income")
    private BigDecimal total;

    @JsonProperty("product_sold_count")
    private Long productSoldCount;

    @JsonProperty("top_customer")
    private AccountGetDto topCustomer;

    @JsonProperty("customer_orders")
    private List<CustomerOrderGetDto> customerOrderGetDtoList;


}
