package com.coldev.estore.application.controller;

import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.dashboard.response.Dashboard;
import com.coldev.estore.domain.service.DashboardService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.time.Year;

@RestController
@RequestMapping("/api/v1/dashboard")
@Log4j2
public class DashboardController {

    private final DashboardService dashboardService;


    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping()
    public ResponseEntity<?> loadDashboard
            (@RequestParam(required = false) Month month,
             @RequestParam(required = false) Year year) {

        Dashboard dashboard = dashboardService.loadDashboard(month, year);

        if (dashboard != null
                && dashboard.getCustomerOrderGetDtoList() != null
                && !dashboard.getCustomerOrderGetDtoList().isEmpty()) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .data(dashboard)
                            .totalItems(1)
                            .message(MessageDictionary.DATA_FOUND)
                            .build()
            );
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseObject.builder()
                        .data(dashboard)
                        .message(MessageDictionary.DATA_NOT_FOUND)
                        .totalItems(0)
                        .build()
        );


    }
}
