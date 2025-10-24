package com.example.auth.security.otpauthentication;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

/**
 * @author Arfat A. Chaus
 * since 2025-10-21
 */
public class OTPAuthenticationConverter implements AuthenticationConverter {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        String otp = request.getParameter(OTPConstants.OTP_PARAMETER_NAME);
        try {

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!StringUtils.hasText(otp)) {
                this.logger.debug("No otp found in request");
                return null;
            } else if (principal == null) {
                this.logger.debug("No principal found in request");
                return null;
            } else {
                return OTPAuthenticationToken.unauthenticated(principal, otp);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
