package com.example.flabcaloriecountergateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.LoggingFilterConfig> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter() {
        super(LoggingFilterConfig.class);
    }

    @Override
    public GatewayFilter apply(LoggingFilterConfig config) {
        return new OrderedGatewayFilter((exchange, chain) ->
                loggingFilterMono(config, exchange, chain), OrderedGatewayFilter.HIGHEST_PRECEDENCE
        );
    }

    private Mono<Void> loggingFilterMono(LoggingFilterConfig config, ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        logger.info("Logging Filter baseMessage: {}", config.baseMessage());
        if (config.preLogger()) {
            logger.info("Logging Filter Start: request id -> {}", request.getId());
        }

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            if (config.postLogger()) {
                logger.info("Logging Filter End: response code -> {}", response.getStatusCode());
            }
        }));
    }

    public record LoggingFilterConfig(String baseMessage, boolean preLogger, boolean postLogger) {
    }
}
