package com.coldev.estore.config.exception.mapper;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.account.request.AccountPostDto;
import com.coldev.estore.domain.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Date;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
        //, uses = {AddressMapper.class, PasswordValidator.class, FormMapper.class}
)
public interface AccountMapper {

    default Account.AccountBuilder toNewAccountBuilder(AccountPostDto payload) {
        if (payload == null) return null;

        return Account.builder()
                .username(payload.getUsername())
                //.password(payload.getPassword())
                .email(payload.getEmail())
                .phone(payload.getPhone())
                .name(payload.getName())
                .address(payload.getAddress())
                .status(Status.ACTIVE)
                .createdAt(new Date());
    }

}
