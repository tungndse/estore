package com.coldev.estore.config.security.user;

import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EstoreUserDetailService implements UserDetailsService {

    @Autowired
    AccountService accountService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.findAccountByUsername(username);
        return EstoreUserPrincipal.userBuild(account);
    }
}
