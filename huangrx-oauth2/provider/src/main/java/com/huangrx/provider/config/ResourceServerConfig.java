package com.huangrx.provider.config;

import com.huangrx.provider.handler.MyAuthenticationSuccessHandler;
import com.huangrx.provider.handler.SmsCodeFilter;
import com.huangrx.provider.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author    hrenxiang
 * @since     2022/6/10 10:44
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 表单登录
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/code/sms", "/login/mobile").permitAll()
                .and()
                .formLogin()
                // 处理表单登录 URL
                .loginProcessingUrl("/login")
                // 处理登录成功
                .successHandler(authenticationSuccessHandler)
                .and()
                .authorizeRequests() // 授权配置
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                .and()
                .csrf()
                .disable()
                .addFilterBefore(smsCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public SmsCodeFilter smsCodeFilter() {
        return new SmsCodeFilter();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {return new JwtTokenUtil();}

}