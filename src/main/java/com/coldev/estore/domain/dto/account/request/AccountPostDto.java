package com.coldev.estore.domain.dto.account.request;


import com.coldev.estore.common.enumerate.AccountRole;
import com.coldev.estore.config.validation.EmailValidation;
import com.coldev.estore.config.validation.UsernameValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountPostDto {

    @UsernameValidation
    @NotBlank
    private String username;

    //@PasswordValidation
    @NotBlank
    private String password;

    @JsonProperty("name")
    @NotBlank
    private String name;

    @NotNull
    private AccountRole role;

    //@PhoneValidation
    @NotBlank
    private String phone;

    @EmailValidation
    @NotBlank
    private String email;

    @Nullable
    private String address;

    @Nullable
    @JsonProperty("avatar_media_link")
    private List<String> avatarMediaKeys;

}
