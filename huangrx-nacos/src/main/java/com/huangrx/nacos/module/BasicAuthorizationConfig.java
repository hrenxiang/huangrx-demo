package com.huangrx.nacos.module;

import com.github.reinert.jjschema.Attributes;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author        hrenxiang
 * @since         2022-10-14 14:04:20
 */
@Data
@Component
@ConfigurationProperties(prefix = "basic.authorization.config")
public class BasicAuthorizationConfig {

    @Attributes(title = "url")
    private String url;

    @Attributes(title = "接口调用耗时警告阈值（毫秒数）", minimum = 1000)
    private Integer warningDuration;

}
