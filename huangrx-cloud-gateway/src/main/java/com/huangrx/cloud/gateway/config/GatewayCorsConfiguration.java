package com.huangrx.cloud.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置，nacos中也可配置
 *
 * @author hrenxiang
 * @since 2022-10-17 17:55:49
 */
@Configuration
public class GatewayCorsConfiguration {
//    @Bean
//    public CorsWebFilter corsFilter() {
//        //CorsConfiguration 相关配置说明
//        // 是否允许携带cookies
//        // private Boolean allowCredentials;
//        // 允许的请求源
//        // private List<String> allowedOrigins;
//        // 允许的http方法
//        // private List<String> allowedMethods;
//        // 允许的请求头
//        // private List<String> allowedHeaders;
//        CorsConfiguration config = new CorsConfiguration();
//        // 如果设置为true，addAllowedOrigin就不能使用*了
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }
}