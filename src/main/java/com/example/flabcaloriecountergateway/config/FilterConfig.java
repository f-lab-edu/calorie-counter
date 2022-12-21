package com.example.flabcaloriecountergateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class FilterConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder rlbuilder) {
        String sessionId = getSessionId();
        return rlbuilder.routes()
                .route(r -> r.path("/v1/**")
                        .filters(f -> f.addRequestHeader("x-session-id", sessionId)
                                .addResponseHeader("x-session-id", sessionId))
                        .uri("http://localhost:8080"))
                .route(r -> r.path("/users/**")
                        .filters(f -> f.addRequestHeader("x-session-id", sessionId)
                                .addResponseHeader("x-session-id", sessionId))
                        .uri("http://localhost:8080"))
                .build();
    }

    public String getSessionId() {
        return UUID.randomUUID().toString();
    }
}
