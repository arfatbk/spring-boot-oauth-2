package com.example.auth.security.otpauthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Arfat A. Chaus
 * since 2025-10-21
 */
@Component
public class OTPAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final UserDetailsService userDetailsService;

    public OTPAuthenticationFilter(UserDetailsService userDetailsService) {
        super(PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, OTPConstants.OTP_URL));
        this.userDetailsService = userDetailsService;
        this.setAuthenticationConverter(new OTPAuthenticationConverter());

        this.setAuthenticationManager(new ProviderManager(new OTPAuthenticationProvider()));
        this.setAuthenticationFailureHandler(
                new AuthenticationEntryPointFailureHandler(
                        new LoginUrlAuthenticationEntryPoint(OTPConstants.OTP_URL)
                ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }
}
