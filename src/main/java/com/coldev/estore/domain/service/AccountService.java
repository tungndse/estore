package com.coldev.estore.domain.service;

import com.coldev.estore.domain.entity.Account;

public interface AccountService {

    Account getAccountByUsername(String username);
    Account getAccountById(Long id);


}
