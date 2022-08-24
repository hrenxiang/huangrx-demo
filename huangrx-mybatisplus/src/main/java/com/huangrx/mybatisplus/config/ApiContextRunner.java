package com.huangrx.mybatisplus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文工具辅助类，实际生产中一般在 security的相关配置中进行设置
 *
 * @author hrenxiang
 * @since 2022-08-24 15:11
 */
@Component
@Slf4j
public class ApiContextRunner implements ApplicationRunner {

    private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_TENANT_ID";
    private static final Map<String, Object> CONTEXT = new ConcurrentHashMap<>();

    public void setCurrentTenantId(Integer providerId) {
        CONTEXT.put(KEY_CURRENT_TENANT_ID, providerId);
    }

    public Integer getCurrentTenantId() {
        return (Integer) CONTEXT.get(KEY_CURRENT_TENANT_ID);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("runner start ... ...");
        this.setCurrentTenantId(1);
    }
}
