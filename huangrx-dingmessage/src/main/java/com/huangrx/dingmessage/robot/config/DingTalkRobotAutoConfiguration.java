package com.huangrx.dingmessage.robot.config;

import com.huangrx.dingmessage.robot.client.DingTalkRobotClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * DingTalk机器人的AutoConfiguration，默认自动化加载一些配置
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:43
 */
@Configuration
public class DingTalkRobotAutoConfiguration {

    /**
     * 创建一个RestTemplate客户端，并注册到Spring容器，供后续程序调用
     * @return RestTemplate客户端
     */
    @Bean(name = "dingTalkRobotRestTemplate")
    @ConditionalOnMissingBean
    public RestTemplate dingTalkRobotRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 创建钉钉机器人本地调用客户端，并注册到Spring容器
     * @return 钉钉机器人本地调用客户端
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(name = "dingTalkRobotRestTemplate")
    public DingTalkRobotClient dingTalkRobotClient() {
        return new DingTalkRobotClient();
    }
}