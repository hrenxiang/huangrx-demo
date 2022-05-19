package com.huangrx.threadlocal.handler;

import com.huangrx.threadlocal.config.Container;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 *
 * @author hrenxiang
 * @since 2022-05-19 3:25 PM
 */

public class RequestHeadInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取页面当前语言
        Container.threadLocal().set(request.getHeader("Host"));
        return Boolean.TRUE;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Container.threadLocal().remove();
    }
}
