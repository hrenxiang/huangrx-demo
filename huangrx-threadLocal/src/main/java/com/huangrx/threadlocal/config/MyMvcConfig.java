package com.huangrx.threadlocal.config;

import com.huangrx.threadlocal.handler.RequestHeadInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * wevMvc
 *
 * @author hrenxiang
 * @since 2022-05-19 3:32 PM
 */
@Component
public class MyMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestHeadInterceptor())
                .addPathPatterns("/*");
        super.addInterceptors(registry);
    }
}
