package com.coldev.estore.domain.service;

import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderPostDto;
import com.coldev.estore.domain.entity.CustomerOrder;

public interface CustomerOrderService {
    CustomerOrder createCustomerOrder(CustomerOrderPostDto customerOrderPostDto);
}
