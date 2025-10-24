package com.example.auth.security.otpauthentication;

import com.example.auth.security.user.DelegatingUserDetails;
import dev.samstevens.totp.code.CodeVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OTPAuthenticationProvider.class);

    private final CodeVerifier verifier;

    public OTPAuthenticationProvider(CodeVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OTPAuthenticationToken token = (OTPAuthenticationToken) authentication;
        String otp = token.getOtp();
        if (logger.isTraceEnabled()) {
            logger.trace("Authenticating OTP: {}", otp);
        }

        try {
            var authn = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            if (authn == null || authn.getPrincipal() == null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("No prior authentication found in SecurityContext authn : {}", authn);
                }
                throw new BadCredentialsException("No prior authentication found");
            }

            var secret = ((DelegatingUserDetails) authn.getPrincipal()).getTotpSecret();
            if (!verifier.isValidCode(secret, otp)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Invalid OTP code provided: {}", otp);
                }
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
            if (logger.isTraceEnabled()) {
                logger.trace("OTP authentication successful for user: {}", authn.getName());
            }
            return authenticated;
        } catch (Exception e) {
            if (logger.isTraceEnabled()) {
                logger.trace("OTP authentication failed with exception", e);
            }
            throw new BadCredentialsException("Bad credentials", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OTPAuthenticationToken.class.isAssignableFrom(authentication);
    }
}