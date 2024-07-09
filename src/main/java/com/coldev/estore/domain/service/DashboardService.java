package com.coldev.estore.domain.service;

import com.coldev.estore.domain.dto.dashboard.response.Dashboard;

import java.time.Month;
import java.time.Year;

public interface DashboardService {


    Dashboard loadDashboard(Month month, Year year);
}
