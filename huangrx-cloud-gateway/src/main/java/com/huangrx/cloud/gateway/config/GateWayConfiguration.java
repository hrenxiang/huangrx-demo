package com.huangrx.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author hrenxiang
 * @since 2022-10-17 16:48
 */
@Configuration
public class GateWayConfiguration {

    /**
     * 限流 默认redis
     * @return KeyResolver
     */
    @Bean("pathKeyResolver")
    public KeyResolver getKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getURI().getPath()
        );
    }

}
