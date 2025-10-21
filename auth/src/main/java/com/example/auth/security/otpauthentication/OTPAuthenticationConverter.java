package com.example.auth.security.otpauthentication;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
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
        String username = request.getParameter(OTPConstants.OTP_PRINCIPAL_NAME);
        if (!StringUtils.hasText(otp)) {
            this.logger.debug("No otp found in request");
            return null;
        } else if (!StringUtils.hasText(username)) {
            this.logger.debug("No username found in request");
            return null;
        } else {
            return OTPAuthenticationToken.unauthenticated(username, otp);
        }
    }
}
