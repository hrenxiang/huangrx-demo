package com.huangrx.security.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * 动态权限相关业务类
 * @author    hrenxiang
 * @since     2022/5/11 10:29 AM
 */
public interface DynamicSecurityService {
    /**
     * 加载资源ANT通配符和资源对应MAP
     * @return  Map<String, ConfigAttribute>
     */
    Map<String, ConfigAttribute> loadDataSource();
}
