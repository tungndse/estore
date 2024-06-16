package com.coldev.estore.infrastructure.service.implementation;


import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.AccountRole;
import com.coldev.estore.config.exception.general.DuplicatedException;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.mapper.AccountMapper;
import com.coldev.estore.domain.dto.account.request.AccountPostDto;
import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.infrastructure.repository.AccountRepository;
import com.coldev.estore.infrastructure.repository.specification.AccountSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    public AccountServiceImpl(
            AuthenticationManager authenticationManager,
            AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountMapper accountMapper) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Account createAccount(AccountPostDto payload, boolean isAuthorized) {

        if (accountRepository.exists(AccountSpecifications.hasUsername(payload.getUsername())))
            throw new DuplicatedException(MessageDictionary.DUPLICATED_USERNAME);

        if (accountRepository.exists(AccountSpecifications.hasEmail(payload.getEmail())))
            throw new DuplicatedException(MessageDictionary.DUPLICATED_EMAIL);

        String encodedPassword = passwordEncoder.encode(payload.getPassword());

        Account.AccountBuilder accountBuilder = accountMapper.toNewAccountBuilder(payload)
                .role(isAuthorized ? payload.getRole() : AccountRole.CUSTOMER)
                .password(encodedPassword);

        if (payload.getAvatarMediaKeys() != null) {
            //TODO handling media here
        }

        return accountRepository.save(accountBuilder.build());

    }


}
