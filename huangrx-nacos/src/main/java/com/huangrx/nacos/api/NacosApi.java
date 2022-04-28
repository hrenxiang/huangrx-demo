package com.huangrx.nacos.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 报漏接口
 *
 * @author hrenxiang
 * @since 2022-04-28 6:53 PM
 */
public interface NacosApi {
    /**
     * 测试feign
     *
     * @return 字符串
     */
    @GetMapping("/get")
    String get();
}
