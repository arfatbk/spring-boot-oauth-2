package com.example.auth.security.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(RegisteredClientsConfiguration.OAuth2RegisteredClients.class)
class RegisteredClientsConfiguration {

    @ConfigurationProperties(prefix = "app.security.oauth2")
    public record OAuth2RegisteredClients(Map<String, OAuth2ClientProperties> clients) {
    }

    public record OAuth2ClientProperties(
            String clientId,
            String clientSecret,
            String redirectUri,
            String postLogoutRedirectUri,
            List<String> scopes,
            Duration refreshTokenTimeToLive,
            Duration accessTokenTimeToLive
    ) {
    }
}
