package com.example.auth.security;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
@Configuration
@Import(SecurityConfig.TOTPConfigurer.class)
public class SecurityConfig {


    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Configuration
    static class TOTPConfigurer {
        @Bean
        SecretGenerator secretGenerator() {
            return new DefaultSecretGenerator();
        }

        @Bean
        QrDataFactory qrDataFactory() {
            return new QrDataFactory(HashingAlgorithm.SHA256, 6, 30);
        }

        @Bean
        QrGenerator qrGenerator() {
            return new ZxingPngQrGenerator();
        }

        @Bean
        CodeVerifier codeVerifier() {
            DefaultCodeVerifier defaultCodeVerifier = new DefaultCodeVerifier(codeGenerator(), timeProvider());
            defaultCodeVerifier.setAllowedTimePeriodDiscrepancy(0);
            return defaultCodeVerifier;
        }

        @Bean
        CodeGenerator codeGenerator() {
            return new DefaultCodeGenerator();
        }

        @Bean
        TimeProvider timeProvider() {
            return new SystemTimeProvider();
        }
    }
}