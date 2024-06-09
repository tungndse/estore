package com.coldev.estore.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, DefaultAuthenticationEventPublisher authenticationEventPublisher) throws Exception {
        return null;
        /*httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationEventPublisher(authenticationEventPublisher())
                .userDetailsService()*/

    }


}
