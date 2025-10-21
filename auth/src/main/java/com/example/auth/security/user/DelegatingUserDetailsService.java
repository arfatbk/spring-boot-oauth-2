package com.example.auth.security.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
@Service
class DelegatingUserDetailsService implements UserDetailsService { //extends JdbcUserDetailsManager implements UserDetailsService {

    private final UserDetailsService delegate;

    private DelegatingUserDetailsService() {
//        this.userDetailsService = new JdbcUserDetailsManager();
        this.delegate = inMemoryUserDetailsManager();
    }


    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        var user = User.withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build();

        var admin = User.withUsername("admin")
                .password("{noop}password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.delegate.loadUserByUsername(username);
    }
}
