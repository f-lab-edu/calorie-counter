package com.example.flabcaloriecountergateway.config;

import com.example.flabcaloriecountergateway.filter.CustomFilter;
import com.example.flabcaloriecountergateway.filter.LoggingFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class FilterConfig {

    private final CustomFilter customFilter;
    private final LoggingFilter loggingFilter;

    public FilterConfig(CustomFilter customFilter, LoggingFilter loggingFilter) {
        this.customFilter = customFilter;
        this.loggingFilter = loggingFilter;
    }

    @Bean
    // Bean 등록
    public RouteLocator gatewayRoutes(RouteLocatorBuilder rlbuilder) {
        String sessionId = getSessionId();
        GatewayFilter appliedCustomFilter = customFilter.apply(customFilterImpl());
        GatewayFilter appliedLoggingFilter = loggingFilter.apply(loggingFilterConfigImpl());
        return rlbuilder.routes()
                .route(r -> r.path("/v1/**")
                        .filters(f -> f.filter(appliedCustomFilter).filter(appliedLoggingFilter).addRequestHeader("x-session-id", sessionId)
                                .addResponseHeader("x-session-id", sessionId))
                        .uri("http://localhost:8080"))
                .build();
    }

    private CustomFilter.CustomFilterConfig customFilterImpl() {
        return new CustomFilter.CustomFilterConfig();
    }

    private LoggingFilter.LoggingFilterConfig loggingFilterConfigImpl() {
        return new LoggingFilter.LoggingFilterConfig("Custom Pre Filter", true, true);
    }

    public String getSessionId() {
        return UUID.randomUUID().toString();
    }
}
