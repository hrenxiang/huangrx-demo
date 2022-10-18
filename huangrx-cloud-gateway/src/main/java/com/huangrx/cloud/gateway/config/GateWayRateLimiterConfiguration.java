package com.huangrx.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 流控 限流
 *
 * @author hrenxiang
 * @since 2022-10-17 16:48
 */
@Configuration
public class GateWayRateLimiterConfiguration {

    /**
     * 根据 uri限流
     *
     * @return KeyResolver
     */
    @Bean("pathKeyResolver")
    public KeyResolver getKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getURI().getPath()
        );
    }

    /**
     * 根据 ip限流
     *
     * @return KeyResolver
     */
//    @Bean("ipKeyResolver")
//    public KeyResolver ipKeyResolver() {
//        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
//    }

}
