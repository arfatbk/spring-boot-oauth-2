package com.example.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfiguration {

    private final List<String> allowedOrigins;
    private final List<String> allowedMethods;
    private final List<String> allowedHeaders;
    private final Long maxAge;

    public SecurityFilterChainConfiguration(
            @Value("${app.cors.allowed-origins}") List<String> allowedOrigins,
            @Value("${app.cors.allowed-methods}") List<String> allowedMethods,
            @Value("${app.cors.allowed-headers}") List<String> allowedHeaders,
            @Value("${app.cors.max-age:3600}") Long maxAge) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.maxAge = maxAge;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }
}
