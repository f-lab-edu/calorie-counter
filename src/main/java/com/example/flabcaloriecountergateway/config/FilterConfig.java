package com.example.flabcaloriecountergateway.config;

import com.example.flabcaloriecountergateway.filter.CustomFilter;
import com.example.flabcaloriecountergateway.filter.CustomFilterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class FilterConfig {

    @Autowired
    private CustomFilter customFilter;

    @Bean
    // Bean 등록
    public RouteLocator gatewayRoutes(RouteLocatorBuilder rlbuilder) {
        String sessionId = getSessionId();
        GatewayFilter appliedFilter = customFilter.apply(new CustomFilterConfig());
        return rlbuilder.routes()
                .route(r -> r.path("/v1/**")
                        .filters(f -> f.filter(appliedFilter).addRequestHeader("x-session-id", sessionId)
                                .addResponseHeader("x-session-id", sessionId))
                        .uri("http://localhost:8080"))
                .build();
    }

    public String getSessionId() {
        return UUID.randomUUID().toString();
    }
}
