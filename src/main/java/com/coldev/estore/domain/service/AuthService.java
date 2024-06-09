package com.coldev.estore.domain.service;

import com.coldev.estore.domain.dto.login.request.LoginRequest;
import com.coldev.estore.domain.dto.login.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

}
