package com.huangrx.i18n.config;

import com.huangrx.i18n.inteceptor.PageLanguageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author        hrenxiang
 * @since         2022-08-17 11-17-05
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        // 下面两种方式配置一种即可
        //(1)Cookie方式
        /* CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("localeCookie");
        //设置默认区域
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        localeResolver.setCookieMaxAge(3600);//设置cookie有效期.
        return localeResolver;*/
    
        //(2)Session方式
        SessionLocaleResolver slr = new SessionLocaleResolver();
        // 默认语言 使用 zh_CN
        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名实现国际化效果
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public PageLanguageHandler pageLanguageHandler() {
        return new PageLanguageHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(pageLanguageHandler());
    }
}