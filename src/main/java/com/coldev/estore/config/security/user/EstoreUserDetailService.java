package com.coldev.estore.config.security.user;

import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EstoreUserDetailService implements UserDetailsService {

    private final
    AccountService accountService;

    public EstoreUserDetailService(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.findAccountByUsername(username);
        return EstoreUserPrincipal.userBuild(account);
    }
}
