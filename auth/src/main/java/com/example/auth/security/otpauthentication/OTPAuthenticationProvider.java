package com.example.auth.security.otpauthentication;

import com.example.auth.security.user.DelegatingUserDetails;
import dev.samstevens.totp.code.CodeVerifier;
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
//@Service
public class OTPAuthenticationProvider implements AuthenticationProvider {

    private final CodeVerifier verifier;

    public OTPAuthenticationProvider(CodeVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OTPAuthenticationToken token = (OTPAuthenticationToken) authentication;
        String otp = token.getOtp();


        try {
            var authn = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            if (authn == null || authn.getPrincipal() == null) {
                throw new BadCredentialsException("No prior authentication found");
            }

            var secret = ((DelegatingUserDetails) authn.getPrincipal()).getTotpSecret();
            if (!verifier.isValidCode(secret, otp)) {
                throw new BadCredentialsException("Invalid OTP");
            }

            var otpAuthority = FactorGrantedAuthority
                    .withFactor(OTPConstants.OTP_AUTHORITY_NAME)
                    .build();

            var authorities = new HashSet<GrantedAuthority>();


            authorities.add(otpAuthority);
            authorities.addAll(authn.getAuthorities());

            // Update the SecurityContext with the new authentication

            UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(
                    authn.getPrincipal(), authn.getCredentials(), authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return authenticated;
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OTPAuthenticationToken.class.isAssignableFrom(authentication);
    }
}