package com.huangrx.cloud.gateway.config;

import com.huangrx.cloud.gateway.domain.response.BaseResponse;
import com.huangrx.cloud.gateway.domain.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * 熔断降级
 *
 * @author hrenxiang
 * @since 2022-10-17 18:04:15
 */
@RestController
@Slf4j
public class FallbackConfiguration {

    @RequestMapping(value = "/fallback", method = RequestMethod.GET)
    public BaseResponse<String> fallback(ServerWebExchange exchange) {
        log.error("FallbackConfiguration，接口调用失败，URL={}", exchange.getRequest().getPath().pathWithinApplication().value());
        return BaseResponse.failed(ResultCode.SERVER_ERROR);
    }
}