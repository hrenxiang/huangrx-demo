package com.huangrx.unifiedlog.filter;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * 日志链路追踪 【 logback中输出格式配置 %X[tract_id] 】
 * <p>
 * 有的登陆过滤器可能没有打印出 tract_id, 这个可能是过滤器的执行顺序问题
 * 可以在 HttpSecurity 的 performBuild 方法处打断点查看 排序前和排序后的过滤器
 * <p>
 * MDC：一个线程安全的存放诊断日志的容器，其实底层是维护了一个 ThreadLocal
 * <p>
 * 排序可以使用 @Order，但是有时候不起作用
 * 如果结合了 security，可以在 security的配置类中的 重写的参数为httpSecurity的 configure方法中进行配置
 * 配置方法是：addFilterBefore，实际就是位置-1，addFilterAfter，实际就是位置+1；addFilterAt，当前的位置
 * 可自行查看源码
 *
 * @author hrenxiang
 * @since 2022/8/10 11:48
 */
@Component
public class LogTracingFilter implements Filter {

    private static final String TRACT_ID = "tract_id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put(TRACT_ID, UUID.randomUUID().toString().replace("-", ""));
            chain.doFilter(request, response);
        } finally {
            MDC.remove(TRACT_ID);
        }
    }

    @Override
    public void destroy() {
    }
}