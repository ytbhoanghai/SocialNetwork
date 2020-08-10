package com.nguyenhai.demo.Security;

import com.nguyenhai.demo.Entity.Account;
import com.nguyenhai.demo.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private AccountService accountService;

    @Autowired
    public CustomUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return buildUserDetails(email);
    }

    public UserDetails buildUserDetails(String email) {
        Account account = accountService.findByEmail(email);
        List<GrantedAuthority> authorities = account.getRoles().stream()
                .map(e -> new SimpleGrantedAuthority("ROLE_" + e))
                .collect(Collectors.toList());

        return new User(
                account.getEmail(),
                account.getPassword(),
                account.getIsEnabled(),
                account.getIsAccountNonExpired(),
                account.getIsCredentialsNonExpired(),
                account.getIsAccountNonLocked(),
                authorities);
    }
}
