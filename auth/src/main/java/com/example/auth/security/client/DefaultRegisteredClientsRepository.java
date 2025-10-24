package com.example.auth.security.client;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DefaultRegisteredClientsRepository {

    private final RegisteredClientsConfiguration.OAuth2RegisteredClients oAuth2RegisteredClients;

    public DefaultRegisteredClientsRepository(
            RegisteredClientsConfiguration.OAuth2RegisteredClients oAuth2RegisteredClients) {
        this.oAuth2RegisteredClients = oAuth2RegisteredClients;
    }


    @Bean
    public RegisteredClientRepository registeredClientRepository() {


        var registerdClients = oAuth2RegisteredClients.clients()
                .values()
                .stream().map(oAuth2Client -> {
                    return RegisteredClient.withId(UUID.randomUUID().toString())
                            .clientId(oAuth2Client.clientId())
                            .clientSecret(oAuth2Client.clientSecret())
                            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                            .redirectUri(oAuth2Client.redirectUri())
                            .postLogoutRedirectUri(oAuth2Client.postLogoutRedirectUri())
                            .scopes(scopes -> scopes.addAll(oAuth2Client.scopes()))
                            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                            .tokenSettings(TokenSettings.builder()
                                    .reuseRefreshTokens(false)
                                    .refreshTokenTimeToLive(
                                            oAuth2Client.refreshTokenTimeToLive()
                                    )
                                    .accessTokenTimeToLive(
                                            oAuth2Client.accessTokenTimeToLive()
                                    )
                                    .build())
                            .build();
                })
                .toList();

        return new InMemoryRegisteredClientRepository(registerdClients);
    }

}
