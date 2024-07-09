package com.coldev.estore.infrastructure.service.implementation;


import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.AccountRole;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.common.utility.SortUtils;
import com.coldev.estore.common.utility.SpecificationUtils;
import com.coldev.estore.config.exception.general.DuplicatedException;
import com.coldev.estore.config.exception.general.ItemNotFoundException;
import com.coldev.estore.config.exception.mapper.AccountMapper;
import com.coldev.estore.domain.dto.account.request.AccountFilerRequest;
import com.coldev.estore.domain.dto.account.request.AccountPostDto;
import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.entity.Media;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.MediaService;
import com.coldev.estore.infrastructure.repository.AccountRepository;
import com.coldev.estore.infrastructure.repository.specification.AccountSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final MediaService mediaService;

    private final AccountRepository accountRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    public AccountServiceImpl(
            MediaService mediaService, AuthenticationManager authenticationManager,
            AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountMapper accountMapper) {
        this.mediaService = mediaService;
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
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account getAccountByIdWithNullCheck(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id, ConstantDictionary.ACCOUNT));
    }

    @Override
    public AccountGetDto getAccountDto(Long id) {
        Account account = this.getAccountById(id);

        if (account == null)
            throw new ItemNotFoundException(id, ConstantDictionary.ACCOUNT);

        return accountMapper.toAccountGetDto(account);
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

        if (payload.getMediaId() != null) {
            Media avatarMedia = mediaService.getMediaById(payload.getMediaId());
            accountBuilder.media(avatarMedia);
        }

        return accountRepository.save(accountBuilder.build());

    }

    @Override
    public List<AccountGetDto> getAccountDtoList(AccountFilerRequest filterRequest, ResponseLevel responseLevel) {
        Pageable pageable = SortUtils.getPagination(
                filterRequest.getPageSize(), filterRequest.getPageNo(),
                filterRequest.getSortOrder(), filterRequest.getSortAttribute());

        Page<Account> accountPage = this.getAccountPage(filterRequest, pageable);

        return accountPage.stream()
                .map(account -> {
                    AccountGetDto.AccountGetDtoBuilder accountGetDtoBuilder =
                            accountMapper.toAccountGetDtoBuilder(account);

                    switch (responseLevel) {
                        case BASIC -> {
                        }
                        case ONE_LEVEL_DEPTH -> {
                        }
                        case TWO_LEVEL_DEPTH -> {
                        }
                    }

                    return accountGetDtoBuilder.build();
                })
                .toList();
    }

    @Override
    public Page<Account> getAccountPage(AccountFilerRequest filterRequest, Pageable pageable) {
        Specification<Account> specification =
                Specification.allOf(
                        SpecificationUtils.getSpecifications(filterRequest)
                );

        return accountRepository.findAll(specification, pageable);
    }


}
