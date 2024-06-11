package com.coldev.estore.infrastructure.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.AccountRole;
import com.coldev.estore.config.exception.general.BadRequestException;
import com.coldev.estore.config.security.user.EstoreUserPrincipal;
import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.service.AccountService;
import com.coldev.estore.domain.service.AuthService;
import com.coldev.estore.domain.dto.login.request.LoginRequest;
import com.coldev.estore.domain.dto.login.response.LoginResponse;
import com.coldev.estore.domain.service.JwtService;
import com.coldev.estore.infrastructure.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Log4j2
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.secretKey}")
    static String secret = "chainblade";
    private Algorithm algorithm;

    // BEANS
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final HttpServletRequest request;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AccountService accountService, AuthenticationManager authenticationManager, JwtService jwtService, HttpServletRequest request, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.request = request;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse requestLogin(LoginRequest loginRequest) throws ExecutionException, InterruptedException {

        LoginResponse loginResponse;
        String username = loginRequest.getUsername();

        if (!loginRequest.isLoginWithEmail()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            /*
            The SecurityContextHolder is where Spring Security stores the details of who is authenticated.
            Spring Security does not care how the SecurityContextHolder is populated.
            If it contains a value, it is used as the currently authenticated user.
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);

            EstoreUserPrincipal userPrincipal = (EstoreUserPrincipal) authentication.getPrincipal();
            String accessToken = jwtService.createAccessToken(userPrincipal);
            String refreshToken = jwtService.createRefreshToken(userPrincipal);
            Account account = accountService.getAccountByUsername(username);

            loginResponse = LoginResponse.builder()
                    .accountId(account.getId()).role(account.getRole())
                    .accessToken(accessToken).refreshToken(refreshToken)
                    .build();

        } else { // CASE: Login with email, not supported yet
            loginResponse = null;
        }

        return loginResponse;
    }

    public String getNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String accessToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                algorithm = Algorithm.HMAC256(secret.getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);

                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("role").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                EstoreUserPrincipal userPrincipal = new EstoreUserPrincipal();
                userPrincipal.setUsername(username);
                userPrincipal.setAuthorities(authorities);
                accessToken = jwtService.createAccessToken(userPrincipal);

            } catch (TokenExpiredException e) {
                log.error("Error logging in: {}", e.getMessage());
                response.setHeader("Error", e.getMessage());
                response.sendError(FORBIDDEN.value(), MessageDictionary.TOKEN_EXPIRED);
            } catch (Exception e) {
                log.error("Error logging in: {}", e.getMessage());
                response.setHeader("Error", e.getMessage());
                response.sendError(FORBIDDEN.value(), MessageDictionary.ACCESS_DENIED);
            }
        }
        return accessToken;
    }

    @Override
    public Long retrieveTokenizedAccountId()
            throws IOException, BadRequestException {
        Long tokenizedAccountId;
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            authorizationHeader = authorizationHeader.substring("Bearer ".length());
            tokenizedAccountId = jwtService.decodeJWT(authorizationHeader).getClaim("accountId").asLong();
        } else throw new BadRequestException(MessageDictionary.BAD_REQUEST);

        return tokenizedAccountId;
    }

    @Override
    public AccountRole retrieveTokenizedAccountRole() throws BadRequestException, IOException {
        AccountRole role;
        Claim roleClaim;
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            authorizationHeader = authorizationHeader.substring("Bearer ".length());
            roleClaim = jwtService.decodeJWT(authorizationHeader).getClaim("roles");
            String roleString = roleClaim.toString().replace("\"", "")
                    .replace("[", "").replace("]", "");
            role = AccountRole.valueOf(roleString);
        } else throw new BadRequestException(MessageDictionary.BAD_REQUEST);

        return role;
    }


}
