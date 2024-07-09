package com.coldev.estore.application.controller;


import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.ResponseLevel;
import com.coldev.estore.common.enumerate.SortType;
import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.config.exception.general.BadRequestException;
import com.coldev.estore.domain.dto.ResponseObject;
import com.coldev.estore.domain.dto.account.request.AccountFilerRequest;
import com.coldev.estore.domain.dto.account.request.AccountPostDto;
import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .totalItems(1)
                        .message(MessageDictionary.DATA_FOUND)
                        .data(accountService.getAccountDto(id))
                        .build()
        );
    }

    @GetMapping("/user-info")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getUserInfo() throws IOException, BadRequestException {
        return ResponseEntity.ok(
                accountService.getAccountDto(authService.retrieveTokenizedAccountId())
        );
    }

    @GetMapping()
    public ResponseEntity<ResponseObject<List<AccountGetDto>>> find
            (@RequestParam(name = "page", defaultValue = "0") int page,
             @RequestParam(name = "size", defaultValue = "100") int size,
             @RequestParam(name = "sort_by", required = false) String sortBy,
             @RequestParam(name = "sort_type", required = false) SortType sortType,
             @RequestParam(name = "username", required = false) String username,
             @RequestParam(name = "name_contains", required = false) String searchKey,
             @RequestParam(name = "description_contains", required = false) String descriptionContains,
             @RequestParam(name = "email", required = false) String email,
             @RequestParam(name = "phone", required = false) String phone,
             @RequestParam(name = "status", required = false) Status status
            ) throws IOException, BadRequestException {

        AccountFilerRequest filerRequest = AccountFilerRequest.builder()
                .pageNo(page).pageSize(size)
                .sortOrder(sortType).sortAttribute(sortBy)
                .username(username)
                .searchKey(searchKey)
                .descriptionContains(descriptionContains)
                .email(email)
                .phone(phone)
                .status(status)
                .build();

        List<AccountGetDto> accountGetDtoList = accountService.getAccountDtoList(filerRequest, ResponseLevel.BASIC);

        ResponseObject.ResponseObjectBuilder<List<AccountGetDto>> responseBuilder =
                ResponseObject.builder();

        if (!accountGetDtoList.isEmpty()) {
            ResponseObject<List<AccountGetDto>> response = responseBuilder
                    .message(MessageDictionary.DATA_FOUND)
                    .data(accountGetDtoList)
                    .totalItems(accountGetDtoList.size()).build();
            return ResponseEntity.ok(response);
        } else {
            ResponseObject<List<AccountGetDto>> response = responseBuilder.message(MessageDictionary.DATA_NOT_FOUND)
                    .totalItems(0).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    @PostMapping
    @PreAuthorize("permitAll()")
    ResponseEntity<?> register(@RequestBody AccountPostDto payload)
            throws BadRequestException, IOException {
        return ResponseEntity.ok(
                accountService.getAccountDto(accountService.createAccount(payload, false).getId())
        );
    }



}
