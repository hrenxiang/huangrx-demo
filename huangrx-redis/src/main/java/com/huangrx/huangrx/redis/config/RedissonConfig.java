package com.huangrx.huangrx.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson，分布式
 *
 * @author        hrenxiang
 * @since         2022-09-13 18:27:13
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        // 可以用"rediss://"来启用SSL连接
        config.useSingleServer().setAddress("redis://10.58.128.240:6379");
        return Redisson.create(config);
    }
}