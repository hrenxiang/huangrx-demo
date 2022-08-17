package com.huangrx.i18n.inteceptor;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * 语言拦截器
 *
 * @author hrenxiang
 * @since 2022-03-08 2:19 PM
 */
public class PageLanguageHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取页面当前语言
        String lang = request.getHeader("lang");
        Container.PageLanguage.set(lang);
//        String[] s = lang.split("_");
//        LocaleContextHolder.setLocale(new Locale(s[0], s[1]));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Container.PageLanguage.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
