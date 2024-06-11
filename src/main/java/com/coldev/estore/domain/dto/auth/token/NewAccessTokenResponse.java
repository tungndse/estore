package com.coldev.estore.domain.dto.auth.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAccessTokenResponse {

    private String message;
    private String authorization;

}
