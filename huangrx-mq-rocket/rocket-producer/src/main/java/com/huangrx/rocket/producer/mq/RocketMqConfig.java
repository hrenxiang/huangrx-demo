package com.huangrx.rocket.producer.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mq 配置类
 *
 * @author hrenxiang
 * @since 2022-05-23 4:55 PM
 */
@Data
@Component
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMqConfig {

    private String group;
    private String nameServerAddr;
    private Integer sendMsgTimeout;
    private Integer failureRetryNumber;

}
