package com.coldev.estore.domain.service;

import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderPostDto;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderRequestPayload;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.entity.CustomerOrder;

import java.util.List;

public interface CustomerOrderService {

    List<Long> createCustomerOrders(CustomerOrderRequestPayload customerOrderRequestPayload);
    
    List<Long> placeCustomerOrders(List<Long> customerOrderIdList);

    List<CustomerOrderGetDto> getCustomerOrderDtoListByIds(List<Long> createdCustomerOrderIdList);

    CustomerOrder getCustomerOrderById(Long id);

    CustomerOrder getCustomerOrderByIdWithNullCheck(Long id);
}
