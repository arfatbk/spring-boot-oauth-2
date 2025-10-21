package com.example.auth.security;

import com.example.auth.security.otpauthentication.OTPAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
@Configuration
class SecurityFilterChains {


    private final OTPAuthenticationFilter otpAuthenticationFilter;

    public SecurityFilterChains(OTPAuthenticationFilter otpAuthenticationFilter) {
        this.otpAuthenticationFilter = otpAuthenticationFilter;
    }

    //OAuth2 server
    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) {

        http.oauth2AuthorizationServer((authServer) -> {
                    http.securityMatcher(authServer.getEndpointsMatcher());
                    authServer.oidc(Customizer.withDefaults());
                })
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest()
                        .authenticated()
                )
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML))
                );

        return http.build();
    }

    //Default security filter chain
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(AuthzCustomizer.withCustomisations())
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(form -> form.loginPage("/form-login").permitAll())
                .addFilterBefore(otpAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(CustomExceptionHandlingConfigurer.withCustomizations());
        return http.build();

    }

}
