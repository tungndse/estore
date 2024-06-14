package com.coldev.estore.application.controller;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.config.exception.general.BadRequestException;
import com.coldev.estore.domain.dto.auth.token.NewAccessTokenResponse;
import com.coldev.estore.domain.dto.login.request.LoginRequest;
import com.coldev.estore.domain.dto.login.response.LoginResponse;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.AuthService;
import com.coldev.estore.domain.service.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@Log4j2
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AccountService accountService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final JwtService jwtService;

    public AuthController(AuthService authService, AccountService accountService,
                          HttpServletRequest request, HttpServletResponse response, JwtService jwtService) {
        this.authService = authService;
        this.accountService = accountService;
        this.request = request;
        this.response = response;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @SecurityRequirements
    @PreAuthorize("permitAll()")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginForm) throws ExecutionException, InterruptedException {
        LoginResponse loginResponse;
        try {
            loginResponse = authService.requestLogin(loginForm);
            if (loginResponse != null) {
                loginResponse.setMessage(MessageDictionary.LOGIN_SUCCESSFUL);
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            } else {
                loginResponse = LoginResponse.builder().message(MessageDictionary.WRONG_CREDENTIALS_INFORMATION).build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
            }

        } catch (AuthenticationException e) {
            loginResponse = LoginResponse.builder().message(MessageDictionary.WRONG_CREDENTIALS_INFORMATION).build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }

    @GetMapping("/getNewAccessToken")
    @PreAuthorize("permitAll()")
    public ResponseEntity<NewAccessTokenResponse> getNewAccessToken() throws IOException, TokenExpiredException {
        String accessToken;
        NewAccessTokenResponse newAccessToken = new NewAccessTokenResponse();
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null) {
            newAccessToken.setMessage(MessageDictionary.REFRESH_TOKEN_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newAccessToken);
        } else {
            accessToken = authService.getNewAccessToken(request, response);
            newAccessToken.setAuthorization(accessToken);
            newAccessToken.setMessage(MessageDictionary.ACCESS_TOKEN_GRANTED);
            return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
        }
    }

    @GetMapping("/user-info")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getUserInfo() throws IOException, BadRequestException {
        return ResponseEntity.ok(
                accountService.getAccountById(authService.retrieveTokenizedAccountId())
        );
    }
}
