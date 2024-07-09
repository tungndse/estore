package com.coldev.estore.infrastructure.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.config.security.user.EstoreUserPrincipal;
import com.coldev.estore.domain.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Log4j2
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secretKey}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private int accessTokenDuration;

    @Value("${jwt.refreshExpirationMs}")
    private int refreshTokenDuration;

    @Value("${jwt.issuer}")
    private String issuer;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse response;

    private Algorithm algorithm;

    public JwtServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, HttpServletResponse response) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.response = response;
    }

    @Override
    public String createAccessToken(EstoreUserPrincipal userPrincipal) {
        algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withClaim("accountId", userPrincipal.getId())
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenDuration))
                .withIssuer(issuer)
                .withClaim("roles", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }


    @Override
    public String createRefreshToken(EstoreUserPrincipal userPrincipal) {
        algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withClaim("accountId", userPrincipal.getId())
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenDuration))
                //exception
                .withClaim("roles", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    @Override
    public DecodedJWT decodeJWT(String jwt) throws IOException {
        DecodedJWT decodedJWT = null;
        if (jwt != null) {
            try {
                algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                decodedJWT = verifier.verify(jwt);
            } catch (TokenExpiredException e) {
                response.sendError(FORBIDDEN.value(), MessageDictionary.TOKEN_EXPIRED);
            } catch (Exception e) {
                response.sendError(FORBIDDEN.value(), MessageDictionary.ACCESS_DENIED);
            }
        }
        return decodedJWT;
    }



}
