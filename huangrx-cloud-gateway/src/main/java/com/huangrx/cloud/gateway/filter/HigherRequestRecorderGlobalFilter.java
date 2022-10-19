package com.huangrx.cloud.gateway.filter;

import com.huangrx.cloud.gateway.util.ConstantUtil;
import com.huangrx.cloud.gateway.util.GatewayLogUtil;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author hrenxiang
 * @since 2022-10-14 17:33:08
 */
@Component
@ConditionalOnProperty(prefix = "gateway.log", name = "enabled", havingValue = "true")
public class HigherRequestRecorderGlobalFilter implements GlobalFilter, Ordered {

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

        //在 NettyRoutingFilter 之前执行， 基本上属于倒数第二个过滤器了
        //此时的request是 经过各种转换、转发之后的request
        //对应日志中的 代理请求 部分
        RecorderServerHttpRequestDecorator request = new RecorderServerHttpRequestDecorator(originalRequest);
        ServerWebExchange ex = exchange.mutate()
                .request(request)
                .build();
        System.out.println("----------" + MDC.get("trace_id"));
        return GatewayLogUtil.recorderRouteRequest(ex)
                .then(Mono.defer(() -> chain.filter(ex)));
    }

    @Override
    public int getOrder() {
        //在向业务服务转发前执行  NettyRoutingFilter 或 WebClientHttpRoutingFilter
        return Ordered.LOWEST_PRECEDENCE - 10;
    }
}