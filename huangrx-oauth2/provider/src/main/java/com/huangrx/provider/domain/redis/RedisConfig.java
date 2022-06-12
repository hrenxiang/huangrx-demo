package com.huangrx.provider.domain.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis 配置类 spring boot 2.x
 *
 * @author hrenxiang
 * @since 2022-04-24 8:26 PM
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public RedisSerializer<Object> valueSerializer() {
        //创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // https://www.cnblogs.com/shanheyongmu/p/15157378.html
        // objectMapper.enableDefaultTyping()被弃用
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        //objectMapper.activateDefaultTyping(
        //        LaissezFaireSubTypeValidator.instance,
        //        ObjectMapper.DefaultTyping.NON_FINAL,
        //        JsonTypeInfo.As.WRAPPER_ARRAY);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    /**
     * 在springboot 2.x 中，没有 RedisCacheManager rcm = new RedisCacheManager(redisTemplate); 此构造器
     *
     * 我们将使用
     * RedisCacheManager redisCacheManager = RedisCacheManager.create(redisConnectionFactory);
     * 或
     * new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //设置Redis缓存有效期为1天
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //设置key序列化器
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                //设置value序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                //设置缓存的默认超时时间：分钟-ofMinutes  天-ofDays
                .entryTtl(Duration.ofDays(1))
                //如果是空值，不缓存
                .disableCachingNullValues();
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // key 和 value
        // 一般来说， redis-key采用字符串序列化；
        // redis-value采用json序列化， json的体积小，可读性高，不需要实现serializer接口。
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());

        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
