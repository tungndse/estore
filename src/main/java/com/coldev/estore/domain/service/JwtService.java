package com.coldev.estore.domain.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.coldev.estore.config.security.user.EstoreUserPrincipal;

import java.io.IOException;

public interface JwtService {

    String createAccessToken(EstoreUserPrincipal userPrincipal);

    String createRefreshToken(EstoreUserPrincipal userPrincipal);

    DecodedJWT decodeJWT (String jwt) throws IOException;
}
