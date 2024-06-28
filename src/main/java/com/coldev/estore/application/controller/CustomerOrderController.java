package com.coldev.estore.application.controller;


import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderPostDto;
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

@RestController
@RequestMapping("/api/v1/customer-orders")
@Log4j2
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }


    @PostMapping
    public ResponseEntity<?> create
            (@Valid @RequestBody CustomerOrderPostDto customerOrderPostDto) {

        //TODO check token here

        /*CustomerOrderGetDto customerOrderGetDto =
                customerOrderService.getCustomerOrderDtoById(

                        customerOrderService.placeCustomerOrder(
                                customerOrderService.createCustomerOrder(customerOrderPostDto)
                        ).getId(),
                        ResponseLevel.ONE_LEVEL_DEPTH
                );

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.ACTION_SUCCESS)
                        .data(customerOrderGetDto)
                        .build()
        );*/

        return null;
    }


}
