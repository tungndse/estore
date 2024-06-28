package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.domain.dto.customerorder.request.CustomerOrderPostDto;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.service.CustomerOrderService;
import com.coldev.estore.infrastructure.repository.CustomerOrderItemRepository;
import com.coldev.estore.infrastructure.repository.CustomerOrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerOrderItemRepository customerOrderItemRepository;

    public CustomerOrderServiceImpl(CustomerOrderRepository customerOrderRepository, CustomerOrderItemRepository customerOrderItemRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerOrderItemRepository = customerOrderItemRepository;
    }


    @Override
    public CustomerOrder createCustomerOrder(CustomerOrderPostDto customerOrderPostDto) {

        //TODO Check order type: combo or regular?
        // If combo:
        // 1. order item list will be ignore
        // 2. generate order item list based on the combo and replace it with the order item list
        // Else:
        // Foreach item in order item list -> customer order repo > save(item) & put item into temporary list
        // From the temporary list, calculate total item list's price


        return null;


    }
}
