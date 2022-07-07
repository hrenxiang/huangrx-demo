package com.huangrx.rocket.producer.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * mq 配置类
 *
 * @author hrenxiang
 * @since 2022-05-23 4:55 PM
 */
@Data
@Component
@ConfigurationProperties(prefix = "rocket")
public class RocketMqConfig {

    private String group;
    private String nameSrvAddr;
    private Integer sendMsgTimeout;
    private Integer retryTimesWhenSendFailed;

}
