package com.coldev.estore.domain.service;

import com.auth0.jwt.interfaces.Payload;
import com.coldev.estore.domain.dto.account.request.AccountPostDto;
import com.coldev.estore.domain.entity.Account;

public interface AccountService {

    Account getAccountByUsername(String username);
    Account getAccountById(Long id);

    Account createAccount(AccountPostDto payload, boolean isAuthorized);

}
