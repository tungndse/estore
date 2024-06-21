package com.coldev.estore.domain.service;

import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.domain.dto.account.request.AccountFilerRequest;
import com.coldev.estore.domain.dto.account.request.AccountPostDto;
import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface AccountService {

    Account getAccountByUsername(String username);
    Account getAccountById(Long id);
    AccountGetDto getAccountDto(Long id);
    Account createAccount(AccountPostDto payload, boolean isAuthorized);

    List<AccountGetDto> getAccountDtoList(AccountFilerRequest accountFilerRequest, ResponseLevel responseLevel);

    Page<Account> getAccountPage(AccountFilerRequest filterRequest, Pageable pageable);
}
