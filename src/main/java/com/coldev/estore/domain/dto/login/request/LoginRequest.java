package com.coldev.estore.domain.dto.login.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
    private String token;
    //private boolean loginWithEmail;

}
