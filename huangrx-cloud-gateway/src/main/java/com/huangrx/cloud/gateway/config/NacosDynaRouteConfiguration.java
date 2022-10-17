package com.huangrx.cloud.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.huangrx.cloud.gateway.route.NacosRouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nacos 动态路由配置，注入到spring容器中，交给spring进行管理
 *
 * @author hrenxiang
 * @since 2022-10-17 17:30:54
 */
@Configuration
public class NacosDynaRouteConfiguration {
    private final ApplicationEventPublisher publisher;
    private final NacosConfigManager nacosConfigManager;

    public NacosDynaRouteConfiguration(ApplicationEventPublisher publisher, NacosConfigManager nacosConfigManager) {
        this.publisher = publisher;
        this.nacosConfigManager = nacosConfigManager;
    }

    @Bean
    public NacosRouteDefinitionRepository getNacosRouteDefinitionRepository() {
        return new NacosRouteDefinitionRepository(publisher, nacosConfigManager);
    }
}