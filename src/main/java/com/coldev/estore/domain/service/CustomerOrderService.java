package com.coldev.estore.domain.service;

import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderFilterRequest;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderRequestPayload;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.entity.CustomerOrderItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerOrderService {

    List<Long> createCustomerOrders(CustomerOrderRequestPayload customerOrderRequestPayload);
    
    List<Long> placeCustomerOrders(List<Long> customerOrderIdList);

    @Transactional(rollbackFor = Exception.class)
    List<Long> completeCustomerOrders(List<Long> customerOrderIdList);

    List<CustomerOrderGetDto> getCustomerOrderDtoListByIds(List<Long> createdCustomerOrderIdList);

    CustomerOrder getCustomerOrderById(Long id);

    CustomerOrder getCustomerOrderByIdWithNullCheck(Long id);

    List<CustomerOrderGetDto> getCustomerOrderDtoList(CustomerOrderFilterRequest filterRequest, ResponseLevel responseLevel);

    CustomerOrderGetDto getCustomerOrderDtoById(Long id, ResponseLevel responseLevel);

    List<CustomerOrder> findCustomerOrderListBySpecification(Specification<CustomerOrder> specification);

    List<CustomerOrderItem> getCustomerOrderItemList(Long id);

    List<CustomerOrderItem> getCustomerOrderItemListByCustomerOrderIdList(List<Long> customerOrderIdList);


}
