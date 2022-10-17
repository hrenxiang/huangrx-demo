package com.huangrx.cloud.gateway.filter;

import com.huangrx.cloud.gateway.util.ConstantUtil;
import com.huangrx.cloud.gateway.util.GatewayLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

/**
 * @author hrenxiang
 * @since 2022-10-14 17:33:38
 */
@Component
@ConditionalOnProperty(prefix = "gateway.log", name = "enabled", havingValue = "true")
public class LowerRequestRecorderGlobalFilter implements GlobalFilter, Ordered {
    private final Logger logger = LoggerFactory.getLogger(LowerRequestRecorderGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest originalRequest = exchange.getRequest();
        URI originalRequestUrl = originalRequest.getURI();

        //只记录http的请求
        String scheme = originalRequestUrl.getScheme();
        if ((!ConstantUtil.HTTP.equals(scheme) && !ConstantUtil.HTTPS.equals(scheme))) {
            return chain.filter(exchange);
        }

        String upgrade = originalRequest.getHeaders().getUpgrade();
        if (ConstantUtil.WEBSOCKET.equalsIgnoreCase(upgrade)) {
            return chain.filter(exchange);
        }

        // 在 GatewayFilter 之前执行， 此时的request时最初的request
        RecorderServerHttpRequestDecorator request = new RecorderServerHttpRequestDecorator(exchange.getRequest());

        // 此时的response时 发送回客户端的 response
        RecorderServerHttpResponseDecorator response = new RecorderServerHttpResponseDecorator(exchange.getResponse());

        ServerWebExchange ex = exchange.mutate()
                .request(request)
                .response(response)
                .build();

        return GatewayLogUtil.recorderOriginalRequest(ex)
                .then(Mono.defer(() -> chain.filter(ex)))
                .then(Mono.defer(() -> finishLog(ex)));
    }

    private Mono<Void> finishLog(ServerWebExchange ex) {
        return Objects.requireNonNull(GatewayLogUtil.recorderResponse(ex))
                .doOnSuccess(x -> logger.info(GatewayLogUtil.getLogData(ex) + "\n\n\n"));
    }

    @Override
    public int getOrder() {
        //在GatewayFilter之前执行
        return - 1;
    }
}