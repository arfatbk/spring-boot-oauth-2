package com.example.resource.upi.config;

import com.example.resource.tenants.TenantResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientConfiguration {

    private static final String X_TOKEN = "x-token";

    private final TenantResolver tenantResolver;
    private final String upiAutoPayBaseUri;

    public RestClientConfiguration(TenantResolver tenantResolver,
    @Value("${upi.auto-pay.base-uri}") String upiAutoPayBaseUri) {
        this.tenantResolver = tenantResolver;
        this.upiAutoPayBaseUri = upiAutoPayBaseUri;
    }

    @Bean
    RestClient restClient(){
        //TODO: Add timeouts error handling etc
        return RestClient.builder()
                .baseUrl(upiAutoPayBaseUri)
                .defaultHeader(X_TOKEN, tenantResolver.resolveTenantId())
                .build();
    }
}
