package com.coldev.estore.domain.service;

import com.coldev.estore.common.enumerate.AccountRole;
import com.coldev.estore.domain.dto.login.request.LoginRequest;
import com.coldev.estore.domain.dto.login.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface AuthService {

    String getNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    LoginResponse login(LoginRequest loginRequest) throws ExecutionException, InterruptedException;

    Long retrieveTokenizedAccountId() throws IOException, BadRequestException, com.coldev.estore.config.exception.general.BadRequestException;

    AccountRole retrieveTokenizedAccountRole() throws BadRequestException, IOException;

}
