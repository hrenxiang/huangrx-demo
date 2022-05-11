package com.huangrx.mvc.interceptor;

import com.huangrx.mvc.config.Container;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        Container.PageLanguage.set(request.getHeader("lang"));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Container.PageLanguage.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}