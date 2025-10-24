package com.example.auth.security;

import com.example.auth.security.otpauthentication.OTPConstants;
import com.example.auth.security.otpauthentication.OTPFactorGrantedAuthority;
import org.springframework.security.authorization.AuthorizationManagerFactories;
import org.springframework.security.authorization.DefaultAuthorizationManagerFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.authority.FactorGrantedAuthority;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
final class AuthzCustomizer {

    private AuthzCustomizer() {
    }

    private static final DefaultAuthorizationManagerFactory<Object> withUserNamePassword = AuthorizationManagerFactories.multiFactor()
            .requireFactors(FactorGrantedAuthority.PASSWORD_AUTHORITY)
            .build();

    private static final DefaultAuthorizationManagerFactory<Object> withOTP = AuthorizationManagerFactories.multiFactor()
            .requireFactors(
                    FactorGrantedAuthority.PASSWORD_AUTHORITY, OTPFactorGrantedAuthority.FACTOR_OTP
            )
            .build();

    public static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> withCustomisations() {
        return (authorize) -> authorize
                .requestMatchers("/*.css").permitAll()
                .requestMatchers("/*.js").permitAll()
                //Require login to access OTP endpoint
                .requestMatchers(OTPConstants.OTP_URL).access(withUserNamePassword.authenticated())
                .requestMatchers(OTPConstants.OTP_SETUP_URL).access(withUserNamePassword.authenticated())
                //TODO: below verify endpoint not required. testing purpose only
                .requestMatchers("/otp/verify").permitAll()
                //require OTP Factor and ADMIN Role
                .requestMatchers("/admin/**").access(withOTP.hasRole("ADMIN"))
                //require OTP Factor for all other requests
                .anyRequest().access(withOTP.authenticated());

    }

}
