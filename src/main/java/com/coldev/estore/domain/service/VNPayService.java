package com.coldev.estore.domain.service;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {


    String createOrder(int total, String orderInfor, String urlReturn);

    int orderReturn(HttpServletRequest request);
}
