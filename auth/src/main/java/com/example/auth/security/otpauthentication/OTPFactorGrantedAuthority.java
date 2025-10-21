package com.example.auth.security.otpauthentication;

import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;

/**
 * @author Arfat A. Chaus
 * since 2025-10-21
 */
public record OTPFactorGrantedAuthority(String authority, Instant issuedAt) implements GrantedAuthority {

    public static final String FACTOR_OTP = "FACTOR_OTP";

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public static OTPFactorGrantedAuthority build() {
        return new OTPFactorGrantedAuthority(FACTOR_OTP, Instant.now());
    }

    @Override
    public String toString() {
        return "OTPFactorGrantedAuthority{" +
                "authority='" + authority + '\'' +
                ", issuedAt=" + issuedAt +
                '}';
    }
}
