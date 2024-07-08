package com.coldev.estore.domain.dto.dashboard.request;

import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class DashboardCustomRequest {

    private Year year;

    private Month month;

}
