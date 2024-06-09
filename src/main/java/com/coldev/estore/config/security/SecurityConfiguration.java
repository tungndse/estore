package com.coldev.estore.config.security;


import com.coldev.estore.config.security.filter.CustomAuthenticationFilter;
import com.coldev.estore.config.security.filter.CustomAuthorizationFilter;
import com.coldev.estore.config.security.user.EstoreUserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    EstoreUserDetailService userDetailService;
    String[] permittedApiList = {
            "/api/v1/auth/login",
            "/api/v1/auth/getNewAccessToken",
            "/api/v1/accounts/account/registration"
    };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    @SuppressWarnings("removal")
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationEventPublisher(authenticationEventPublisher())
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @SuppressWarnings("removal")
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        CustomAuthenticationFilter filter =
                new CustomAuthenticationFilter(authenticationManager(httpSecurity));

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(filter)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(permittedApiList)
                        .permitAll()
                        .anyRequest().permitAll()
                )
                .exceptionHandling().authenticationEntryPoint(
                        (request, response, exception) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                exception.getMessage()
                        )
                );

        httpSecurity.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
