package com.coldev.estore.domain.service;

import com.coldev.estore.domain.dto.login.request.LoginRequest;
import com.coldev.estore.domain.dto.login.response.LoginResponse;
import com.coldev.estore.domain.entity.Account;

import java.util.concurrent.ExecutionException;

public interface AccountService {

    Account findAccountByUsername(String username);
    Account findAccountById(Long id);

    LoginResponse login(LoginRequest loginRequest) throws ExecutionException, InterruptedException;

}
