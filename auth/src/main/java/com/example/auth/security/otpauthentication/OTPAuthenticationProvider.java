package com.example.auth.security.otpauthentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;

/**
 * @author Arfat A. Chaus
 * since 2025-10-21
 */
public class OTPAuthenticationProvider implements AuthenticationProvider {

    //TODO" remove
    private static final String MOCK_OTP = "123456"; // Mock OTP for validation

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OTPAuthenticationToken token = (OTPAuthenticationToken) authentication;
        String otp = token.getOtp();

        // Simulate OTP validation
        if (!MOCK_OTP.equals(otp)) { //TODO: Replace with real OTP validation logic
            throw new BadCredentialsException("Invalid OTP");
        }

        var authn = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        var otpAuthority = FactorGrantedAuthority
                .withFactor(OTPConstants.OTP_AUTHORITY_NAME)
                .build();

        var authorities = new HashSet<GrantedAuthority>();

        if (authn == null || authn.getPrincipal() == null) {
            throw new BadCredentialsException("No prior authentication found");
        }

        authorities.add(otpAuthority);
        authorities.addAll(authn.getAuthorities());

        // Update the SecurityContext with the new authentication

        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(
                authn.getPrincipal(), authn.getCredentials(), authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        return authenticated;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OTPAuthenticationToken.class.isAssignableFrom(authentication);
    }
}