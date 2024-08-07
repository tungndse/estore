package com.coldev.estore.config.exception.mapper;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.dto.account.request.AccountPostDto;
import com.coldev.estore.domain.dto.account.response.AccountGetDto;
import com.coldev.estore.domain.dto.address.AddressLngLatDto;
import com.coldev.estore.domain.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
        //, uses = {AddressMapper.class, PasswordValidator.class, FormMapper.class}
)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    default Account.AccountBuilder toNewAccountBuilder(AccountPostDto payload) {
        if (payload == null) return null;

        AddressLngLatDto addressCoordination = payload.getAddressLngLatDto();
        Double addressLng = addressCoordination != null ? addressCoordination.getLongitude() : null;
        Double addressLat = addressCoordination != null ? addressCoordination.getLatitude() : null;

        return Account.builder()
                .username(payload.getUsername())
                .description(payload.getDescription())
                .email(payload.getEmail())
                .phone(payload.getPhone())
                .name(payload.getName())
                .address(payload.getAddress())
                .addressLatitude(addressLat)
                .addressLongitude(addressLng)
                .status(Status.ACTIVE)
                .createdAt(new Date());
    }

    default AccountGetDto toAccountGetDto(Account account) {
        return this.toAccountGetDtoBuilder(account).build();
    }

    default AccountGetDto.AccountGetDtoBuilder toAccountGetDtoBuilder(Account account) {
        if (account == null) return null;

        AddressLngLatDto addressLngLatDto = AddressLngLatDto.builder()
                .longitude(account.getAddressLongitude())
                .latitude(account.getAddressLatitude())
                .build();

        AccountGetDto.AccountGetDtoBuilder builder = AccountGetDto.builder()
                .username(account.getUsername())
                .description(account.getDescription())
                .email(account.getEmail())
                .phone(account.getPhone())
                .name(account.getName())
                .address(account.getAddress())
                .addressLngLatDto(addressLngLatDto)
                .status(account.getStatus())
                .createdAt(account.getCreatedAt());

        if (account.getMedia() != null) builder.mediaUrl(account.getMedia().getUrl());

        return builder;
    }

}
