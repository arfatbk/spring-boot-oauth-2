package com.example.gateway.mandates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class MandateRoutes {

    private final String mandatesServiceUrl;

    public MandateRoutes(@Value("${app.services.mandates.url}") String mandatesServiceUrl) {
        this.mandatesServiceUrl = mandatesServiceUrl;
    }

    @Bean
    public RouterFunction<ServerResponse> getMandates() {
        return route()
                .GET("/api/mandates/**", http())
                .before(uri(mandatesServiceUrl))
                .before(rewritePath("/api/(?<segment>.*)", "/${segment}"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> createMandate() {
        return route()
                .POST("/api/mandates/**", http())
                .before(uri(mandatesServiceUrl))
                .before(rewritePath("/api/(?<segment>.*)", "/${segment}"))
                .build();
    }
}
