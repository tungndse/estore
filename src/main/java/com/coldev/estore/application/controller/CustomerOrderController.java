package com.coldev.estore.application.controller;


import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.common.enumerate.PaymentMethod;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.common.enumerate.SortType;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderFilterRequest;
import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderRequestPayload;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.service.CustomerOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            customerOrderService.completeCustomerOrders(createdCustomerOrderIdList);
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

    @GetMapping()
    public ResponseEntity<?> find(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "100") int size,
                                  @RequestParam(name = "status", required = false) OrderStatus status,
                                  @RequestParam(name = "search_key", required = false) String searchKey,
                                  @RequestParam(name = "description_contains", required = false) String descriptionContains,
                                  @RequestParam(name = "sort_by", required = false) String sortBy,
                                  @RequestParam(name = "sort_type", required = false) SortType sortType) {
        CustomerOrderFilterRequest filterRequest = CustomerOrderFilterRequest.builder()
                .pageNo(page).pageSize(size)
                .sortOrder(sortType).sortAttribute(sortBy)
                .status(status)
                .searchKey(searchKey)
                .descriptionContains(descriptionContains)
                .build();


        List<CustomerOrderGetDto> customerOrderGetDtoList = customerOrderService.getCustomerOrderDtoList(
                filterRequest, ResponseLevel.ONE_LEVEL_DEPTH
        );

        ResponseObject.ResponseObjectBuilder<List<CustomerOrderGetDto>> responseBuilder =
                ResponseObject.builder();

        if (!customerOrderGetDtoList.isEmpty()) {
            ResponseObject<List<CustomerOrderGetDto>> response = responseBuilder
                    .message(MessageDictionary.DATA_FOUND)
                    .data(customerOrderGetDtoList)
                    .totalItems(customerOrderGetDtoList.size()).build();
            return ResponseEntity.ok(response);
        } else {
            ResponseObject<List<CustomerOrderGetDto>> response = responseBuilder.message(MessageDictionary.DATA_NOT_FOUND)
                    .totalItems(0).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.DATA_FOUND)
                        .data(customerOrderService.getCustomerOrderDtoById(id, ResponseLevel.ONE_LEVEL_DEPTH))
                        .build()
        );
    }




}
