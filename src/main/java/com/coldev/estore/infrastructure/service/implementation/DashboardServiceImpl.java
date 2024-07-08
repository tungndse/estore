package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.config.exception.mapper.AccountMapper;
import com.coldev.estore.config.exception.mapper.CustomerOrderMapper;
import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.dto.customerorder.response.CustomerOrderGetDto;
import com.coldev.estore.domain.dto.dashboard.response.Dashboard;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.entity.CustomerOrderItem;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.CustomerOrderService;
import com.coldev.estore.domain.service.DashboardService;
import com.coldev.estore.infrastructure.repository.specification.CustomerOrderSpecifications;
import lombok.extern.log4j.Log4j2;
import org.hibernate.query.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DashboardServiceImpl implements DashboardService {

    private final CustomerOrderMapper customerOrderMapper;
    private final AccountMapper accountMapper;
    private final CustomerOrderService customerOrderService;
    private final AccountService accountService;


    public DashboardServiceImpl(CustomerOrderMapper customerOrderMapper, AccountMapper accountMapper, CustomerOrderService customerOrderService, AccountService accountService) {
        this.customerOrderMapper = customerOrderMapper;
        this.accountMapper = accountMapper;
        this.customerOrderService = customerOrderService;
        this.accountService = accountService;
    }


    @Override
    public Dashboard loadDashboard(Month month, Year year) {

        Specification<CustomerOrder> specification = Specification.where(null);

        if (month != null && year != null) {
            // month != null & year != null
            specification = specification
                    .and(CustomerOrderSpecifications.hasYear(year.getValue()))
                    .and(CustomerOrderSpecifications.hasMonth(month.getValue()));
        } else if (year != null) {
            // month = null & year != null
            specification = specification.and(CustomerOrderSpecifications.hasYear(year.getValue()));
        } else if (month == null) {
            // month = null & year = null
            log.info("No year or month input");
        } else {
            // month != null & year = null
            year = Year.now();
            specification = specification.and(CustomerOrderSpecifications.hasCurrentYear())
            .and(CustomerOrderSpecifications.hasMonth(month.getValue()));
        }

        specification = specification.and(CustomerOrderSpecifications.hasStatus(OrderStatus.COMPLETED));

        List<CustomerOrder> customerOrders = customerOrderService.findCustomerOrderListBySpecification(
               specification
        );

        if (customerOrders.isEmpty()) {
            return Dashboard.builder()
                    .month(month)
                    .year(year)
                    .build();
        }

        // Calculate total income
        BigDecimal totalIncome = customerOrders.stream()
                .map(CustomerOrder::getNetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate product sold count
        Long productSoldCount = customerOrders.stream()
                .flatMap(order -> {
                    List<CustomerOrderItem> customerOrderItemList =
                            customerOrderService.getCustomerOrderItemList(order.getId());
                    return customerOrderItemList.stream();
                })
                .mapToLong(CustomerOrderItem::getQuantity)
                .sum();

        // Convert CustomerOrder entities to DTOs
        List<CustomerOrderGetDto> customerOrderGetDtoList = customerOrders.stream()
                .map(order -> customerOrderMapper.toCustomerOrderGetDtoBuilder(order).build())
                .toList();

        // Find the top customer (by total amount spent)
        AccountGetDto topCustomer = customerOrders.stream()
                .collect(Collectors.groupingBy(CustomerOrder::getCustomer, Collectors.reducing(BigDecimal.ZERO, CustomerOrder::getTotalAmount, BigDecimal::add)))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(entry -> accountMapper.toAccountGetDto(entry.getKey()))
                .orElse(null);

        // Build and return the dashboard
        return Dashboard.builder()
                .month(month)
                .year(year)
                .total(totalIncome)
                .productSoldCount(productSoldCount)
                .topCustomer(topCustomer)
                .customerOrderGetDtoList(customerOrderGetDtoList)
                .build();
    }


}
