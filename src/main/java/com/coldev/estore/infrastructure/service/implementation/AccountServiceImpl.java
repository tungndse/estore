package com.coldev.estore.infrastructure.service.implementation;


import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.domain.dto.login.request.LoginRequest;
import com.coldev.estore.domain.dto.login.response.LoginResponse;
import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.infrastructure.repository.AccountRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AuthenticationManager authenticationManager;

    public AccountServiceImpl(
            AuthenticationManager authenticationManager,
            AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new ItemNotFoundException(username));
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.ACCOUNT));

    }


}
