package com.coldev.estore.application.controller;


import com.coldev.estore.config.exception.general.BadRequestException;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/accounts")
@Log4j2
public class AccountController {

    private final AccountService accountService;
    //final AccountMapper accountMapper;

    private final AuthService authService;

    public AccountController(AccountService accountService,
                             //AccountMapper accountMapper,
                             AuthService authService) {
        this.accountService = accountService;
        //this.accountMapper = accountMapper;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() throws IOException, BadRequestException {
        return ResponseEntity.ok(
                accountService.getAccountById(authService.retrieveTokenizedAccountId())
        );
    }

}
