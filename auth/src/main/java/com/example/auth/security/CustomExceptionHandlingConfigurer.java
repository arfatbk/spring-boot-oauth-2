package com.example.auth.security;

import com.example.auth.security.otpauthentication.OTPConstants;
import com.example.auth.security.otpauthentication.OTPFactorGrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
final class CustomExceptionHandlingConfigurer {

    private CustomExceptionHandlingConfigurer() {
    }


    public static Customizer<ExceptionHandlingConfigurer<HttpSecurity>> withCustomizations() {
        return ex -> ex
                .defaultDeniedHandlerForMissingAuthority((ep) -> ep.addEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint(OTPConstants.OTP_URL),
                        PathPatternRequestMatcher.pathPattern(OTPConstants.OTP_URL)
                ), OTPFactorGrantedAuthority.FACTOR_OTP);
    }
}
