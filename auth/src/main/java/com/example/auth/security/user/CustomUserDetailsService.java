package com.example.auth.security.user;

import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
@Service
class CustomUserDetailsService implements UserDetailsService { //extends JdbcUserDetailsManager implements UserDetailsService {

    private final SecretGenerator secretGenerator;
    private final PasswordEncoder passwordEncoder;

    //TODO: for testing only
    private final Map<String, DelegatingUserDetails> delegatingUserDetails = new ConcurrentHashMap<>();

    private CustomUserDetailsService(SecretGenerator secretGenerator, PasswordEncoder passwordEncoder) {
        this.secretGenerator = secretGenerator;
        this.passwordEncoder = passwordEncoder;

        //TODO: for testing only
        this.initInMemoryUsers();
    }

    private void initInMemoryUsers() {
        var user = DelegatingUserDetails
                .username("user")
                .password("{noop}password")
//                .totpSecret(secretGenerator.generate())
                .totpSecret("6ESDDL72AIF7TNDRGHDJKESQPNOUPPC2") //TODO: remove
                .totpSetup(true) //TODO: Default to false, Ask user on first login to setup with QR
                .roles("USER")
                .build();
        this.delegatingUserDetails.put("user", user);


        var admin = (DelegatingUserDetails) DelegatingUserDetails
                .username("admin")
                .password("{noop}password")
                .roles("ADMIN")
                .totpEnabled(false) // TODO:// Can be used to disable for users
                .build();

        this.delegatingUserDetails.put("admin", admin);

    }

    @Override
    public DelegatingUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user =  this.delegatingUserDetails.get(username);
        return DelegatingUserDetails
                .username(user.getUsername())
                .password(user.getPassword())
                .totpSecret(user.getTotpSecret())
                .totpSetup(user.isTotpSetup())
                .roles(user.getAuthorities().stream().map(a -> Objects.requireNonNull(a.getAuthority()).replace("ROLE_", "")).toArray(String[]::new))
                .build();
    }
}
