package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
@EnableWebSecurity
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    @Bean
    public RouterFunction<ServerResponse> getRoute() {
        return route()
                .GET("/api/**", http())
                .before(uri("http://localhost:8081"))
                .before(rewritePath("/api/(?<segment>.*)", "/${segment}"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route()
                .GET("/**", http())
                .before(uri("https://httpbin.org/"))
                .build();
    }
}
