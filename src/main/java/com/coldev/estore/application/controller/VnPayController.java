package com.coldev.estore.application.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/vnpay")
public class VnPayController {

    @PostMapping("/callback")
    public ResponseEntity<String> handleVnpayCallback(@RequestParam Map<String, String> params) {
        // Log the incoming parameters for debugging
        log.info("Received VNPAY callback: " + params);

        // Validate the transaction status and signature here
        String transactionStatus = params.get("vnp_ResponseCode");

        if ("00".equals(transactionStatus)) {
            // Transaction successful
            // Update your order status in the database

            return ResponseEntity.ok("Transaction successful");
        } else {
            // Transaction failed or canceled
            return ResponseEntity.ok("Transaction failed");
        }
    }


}
