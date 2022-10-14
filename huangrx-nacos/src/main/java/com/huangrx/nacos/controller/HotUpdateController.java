package com.huangrx.nacos.controller;

import com.huangrx.nacos.module.BasicAuthorizationConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test hot update config
 *
 * @author hrenxiang
 * @since 2022-10-14 14:57
 */
@RestController
// 可以实时刷新nacos配置 下面@value可以直接获取到新的值
// @RefreshScope
public class HotUpdateController {

    private final BasicAuthorizationConfig basicAuthorizationConfig;

    public HotUpdateController(BasicAuthorizationConfig basicAuthorizationConfig) {
        this.basicAuthorizationConfig = basicAuthorizationConfig;
    }

    /**
     * 第一种热更新 @RefreshScope + @Value("${}")
     *
     * @return nacos 配置数据
     */
    @GetMapping("/testRefreshScope")
    public String testRefreshScope() {
        return basicAuthorizationConfig.getUrl() + "---" + basicAuthorizationConfig.getWarningDuration();
    }

    /**
     * 第二种热更新 @Component + @configurationProperties
     *
     * @return nacos 配置数据
     */
    @GetMapping("/testHotDeploy1")
    public String testConfigurationProperties() {
        return basicAuthorizationConfig.getUrl() + "---" + basicAuthorizationConfig.getWarningDuration();
    }
}
