package com.coldev.estore.domain.dto.login.response;


import com.coldev.estore.common.enumerate.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoginResponse {

    private Long accountId;
    private String message;
    private AccountRole role;

    private String accessToken;
    private String refreshToken;

}
