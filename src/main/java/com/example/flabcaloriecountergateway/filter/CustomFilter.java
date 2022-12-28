package com.example.flabcaloriecountergateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.CustomFilterConfig> {
    private static final Logger logger = LoggerFactory.getLogger(CustomFilter.class);

    public CustomFilter() {
        super(CustomFilterConfig.class);
    }

    @Override
    public GatewayFilter apply(CustomFilterConfig config) {
        return ((exchange, chain) -> {
            // Pre Filter
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            logger.info("Custom Pre Filter: request id -> {}", request.getId());

            // Post Filter
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> logger.info("Custom POST filter: response code -> {}", response.getStatusCode()))
            );
        });
    }

    public static class CustomFilterConfig {
        // Put the configuration properties
    }
}
