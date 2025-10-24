package com.example.auth.security.otpauthentication;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

/**
 * @author Arfat A. Chaus
 * since 2025-10-21
 */
public class OTPAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final String otp;

    protected OTPAuthenticationToken(Object principal, String otp) {
        super(List.of());
        this.principal = principal;
        this.otp = otp;
        setAuthenticated(false);
    }

    protected OTPAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.otp = "[PROTECTED]";
        setAuthenticated(true);
    }

    protected OTPAuthenticationToken(Builder<?> builder) {
        super(builder);
        this.principal = builder.principal;
        this.otp = "[PROTECTED]";
    }

    public static OTPAuthenticationToken unauthenticated(Object principal, String otp) {
        return new OTPAuthenticationToken(principal, otp);
    }


    public static Authentication authenticated(Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new OTPAuthenticationToken(principal, authorities);
    }

    @Override
    public Object getCredentials() {
        return null; // OTP is not stored here
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getOtp() {
        return otp;
    }

    @Override
    public Builder<?> toBuilder() {
        return new Builder<>(this);
    }

    public static class Builder<B extends Builder<B>> extends AbstractAuthenticationBuilder<B> {

        private Object principal;

        protected Builder(OTPAuthenticationToken token) {
            super(token);
            this.principal = token.principal;
        }

        @Override
        public B principal(@Nullable Object principal) {
            Assert.notNull(principal, "principal cannot be null");
            this.principal = principal;
            return (B) this;
        }

        @Override
        public Authentication build() {
            return new OTPAuthenticationToken(this);
        }
    }
}