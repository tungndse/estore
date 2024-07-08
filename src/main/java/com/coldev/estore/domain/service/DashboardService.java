package com.coldev.estore.domain.service;

import com.coldev.estore.common.enumerate.OrderStatus;
import com.coldev.estore.domain.dto.dashboard.response.Dashboard;
import com.coldev.estore.domain.entity.CustomerOrder;

import java.time.Month;
import java.time.Year;
import java.util.List;

public interface DashboardService {


    Dashboard loadDashboard(Month month, Year year);
}
