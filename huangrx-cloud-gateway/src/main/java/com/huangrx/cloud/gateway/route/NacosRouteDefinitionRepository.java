package com.huangrx.cloud.gateway.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import com.huangrx.cloud.gateway.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * gateway 使用nacos实现动态络油
 *
 * @author hrenxiang
 * @since 2022-10-17 14:30
 */
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    /**
     * data id to listen to
     */
    private static final String SCG_ROUTE_DATA_ID = "gateway-route";
    /**
     * data id Indicates the owning group
     */
    private static final String SCG_ROUTE_GROUP_ID = "DEFAULT_GROUP";

    /**
     * Event publisher
     */
    private final ApplicationEventPublisher publisher;
    /**
     * nacos configuration
     * 如果没有 NacosConfigManager 请使用 NacosConfigProperties 获取相关配置
     * NacosConfigProperties获取ConfigService相关方法：nacosConfigProperties.configServiceInstance()
     * 但是高版本中已经废弃configServiceInstance，官方：recommend to use NacosConfigManager.getConfigService().
     * 最后再使用 getConfig即可获取nacos相关配置
     */
    private final NacosConfigManager nacosConfigManager;

    /**
     * Constructor injection
     * @param publisher Event publisher
     * @param nacosConfigManager nacos configuration manager
     */
    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher, NacosConfigManager nacosConfigManager) {
        this.publisher = publisher;
        this.nacosConfigManager = nacosConfigManager;
        addListener();
    }

    /**
     * 添加监听器
     */
    private void addListener() {
        try {
            nacosConfigManager.getConfigService().addListener(SCG_ROUTE_DATA_ID, SCG_ROUTE_GROUP_ID, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            log.error("NacosRouteDefinitionRepository addListener error: {}", e.toString());
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String content = nacosConfigManager.getConfigService().getConfig(SCG_ROUTE_DATA_ID, SCG_ROUTE_GROUP_ID, 5000);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            log.error("NacosRouteDefinitionRepository getRouteDefinitions by nacos error");
        }
        return Flux.fromIterable(Lists.newArrayList());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    private List<RouteDefinition> getListByStr(String content) {
        if (StringUtils.isNotEmpty(content)) {
            return JacksonUtil.parseStringToList(content, RouteDefinition.class);
        }
        return Lists.newArrayList();
    }
}
