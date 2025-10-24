package com.example.auth.security.user;

import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
@Service
class CustomUserDetailsService implements UserDetailsService { //extends JdbcUserDetailsManager implements UserDetailsService {

    private final SecretGenerator secretGenerator;
    private final PasswordEncoder passwordEncoder;

    //TODO: for testing only
    private Map<String, DelegatingUserDetails> delegatingUserDetails = new HashMap<>();

    private CustomUserDetailsService(SecretGenerator secretGenerator, PasswordEncoder passwordEncoder) {
        this.secretGenerator = secretGenerator;
        this.passwordEncoder = passwordEncoder;

        //TODO: for testing only
        this.initInMemoryUsers();
    }

    private void initInMemoryUsers() {
        var user = DelegatingUserDetails
                .username("user")
                .passwordEncoder(passwordEncoder)
                .password("password")
//                .totpSecret(secretGenerator.generate())
                .totpSecret("6ESDDL72AIF7TNDRGHDJKESQPNOUPPC2") //TODO: remove
                .totpSetup(true) //TODO: Default to false, Ask user on first login to setup with QR
                .roles("USER")
                .build();
        this.delegatingUserDetails.put("user", user);


        var admin = (DelegatingUserDetails) DelegatingUserDetails
                .username("admin")
                .passwordEncoder(passwordEncoder)
                .password("password")
                .roles("ADMIN")
                .totpEnabled(false) // TODO:// Can be used to disable for users
                .build();

        this.delegatingUserDetails.put("admin", admin);

    }

    @Override
    public DelegatingUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.delegatingUserDetails.get(username);
    }
}
