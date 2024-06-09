package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.service.AuthService;
import com.coldev.estore.domain.dto.login.request.LoginRequest;
import com.coldev.estore.domain.dto.login.response.LoginResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        LoginResponse loginResponse;
        String username = loginRequest.getUsername();
        Account account;
        

        return null;
    }


}
