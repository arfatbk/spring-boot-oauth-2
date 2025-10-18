package com.example.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@SpringBootApplication
@EnableWebSecurity
public class AuthApplication {

    void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }


    @Configuration
    static class SecurityConfig {


        private static KeyPair generateRsaKey() {
            KeyPair keyPair;
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
            return keyPair;
        }

        @Bean
        @Order(1)
        public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
                throws Exception {
            Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper = (context) -> {
                OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
                JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
                UserDetails userDetails = this.inMemoryUserDetailsManager(null).loadUserByUsername(principal.getName()); //TODO: inject userDetailsService instead
                Map<String, Object> claims = principal.getToken().getClaims();
                claims.put("name", userDetails.getUsername());
                return new OidcUserInfo(claims);
            };
            OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                    OAuth2AuthorizationServerConfigurer.authorizationServer();

            http
                    .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                    .with(authorizationServerConfigurer, (authorizationServer) ->
                            authorizationServer
                                    .oidc((oidc) -> oidc
                                            .userInfoEndpoint((userInfo) -> userInfo
                                                    .userInfoMapper(userInfoMapper)
                                            )
                                    )
                    )
                    .authorizeHttpRequests((authorize) ->
                            authorize
                                    .anyRequest().authenticated()
                    )
                    // Redirect to the login page when not authenticated from the
                    // authorization endpoint
                    .exceptionHandling((exceptions) -> exceptions
                            .defaultAuthenticationEntryPointFor(
                                    new LoginUrlAuthenticationEntryPoint("/login"),
                                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                            )
                    );

            return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
                throws Exception {
            http
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers("/*.css").permitAll()
                            .requestMatchers("/*.js").permitAll()
                            .anyRequest().authenticated()
                    )
                    // Form login handles the redirect to the login page from the
                    // authorization server filter chain
                    .formLogin(form -> form
                            .loginPage("/login").permitAll()
                    );

            return http.build();
        }

        @Bean
        InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder encoder) {
            var user = User
                    .withUsername("user")
                    .password(encoder.encode("password"))
                    .roles("USER")
                    .build();

            return new InMemoryUserDetailsManager(user);
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }


        @Bean
        public RegisteredClientRepository registeredClientRepository() {
            RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("oidc-client")
                    .clientSecret("{noop}secret")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri("http://localhost:8080/login/oauth2/code/oidc-client")
                    .postLogoutRedirectUri("http://localhost:9000/login")
                    .scope(OidcScopes.OPENID)
                    .scope(OidcScopes.PROFILE)
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .build();


            RegisteredClient uiClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("ui-client")
                    .clientSecret("{noop}uisecret")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri("http://localhost:3000/api/auth/callback/custom-oidc")
                    .postLogoutRedirectUri("http://localhost:3000/login")
                    .scope(OidcScopes.OPENID)
                    .scope(OidcScopes.PROFILE)
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                    .build();

            return new InMemoryRegisteredClientRepository(oidcClient, uiClient);
        }

        @Bean
        public JWKSource<SecurityContext> jwkSource() {
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
            JWKSet jwkSet = new JWKSet(rsaKey);
            return new ImmutableJWKSet<>(jwkSet);
        }

        @Bean
        public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
            return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
        }

        @Bean
        public AuthorizationServerSettings authorizationServerSettings() {
            return AuthorizationServerSettings.builder().build();
        }

        @Controller
        class LoginController {
            @GetMapping("/login")
            String login() {
                return "login";
            }
        }

    }
}
