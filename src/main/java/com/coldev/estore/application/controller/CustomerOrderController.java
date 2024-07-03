package com.coldev.estore.application.controller;


import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.PaymentMethod;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderRequestPayload;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/customer-orders")
@Log4j2
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }


    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<?> create
            (@RequestBody CustomerOrderRequestPayload customerOrderRequestPayload) {

        //TODO check token here

        //Check payment method
        PaymentMethod paymentMethod = customerOrderRequestPayload.getPaymentMethod();

        // Create a list of Customer Orders with status ON_HOLD
        List<Long> createdCustomerOrderIdList = customerOrderService.createCustomerOrders(customerOrderRequestPayload);

        // if PaymentMethod = COD
        // -> skip payment part
        // -> placeCustomerOrders(ids) -> change status of all orders into PENDING, save and return the id list
        if (paymentMethod == PaymentMethod.COD) {
            customerOrderService.placeCustomerOrders(createdCustomerOrderIdList);
        }

        // Call service for building a getDtoList for CustomerOrders above
        List<CustomerOrderGetDto> customerOrderGetDtoList = customerOrderService
                .getCustomerOrderDtoListByIds(createdCustomerOrderIdList);

        // Check size of returned, just for sure
        int customerOrderListSize = customerOrderGetDtoList == null || customerOrderGetDtoList.isEmpty() ?
                0 : customerOrderGetDtoList.size();

        // Response
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(customerOrderListSize)
                        .message(customerOrderListSize < 1 ?
                                MessageDictionary.ACTION_FAILED : MessageDictionary.ACTION_SUCCESS
                        )
                        .data(customerOrderGetDtoList)
                        .build()
        );

    }


}
