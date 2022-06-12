package com.huangrx.provider.handler;

import com.huangrx.provider.domain.redis.CacheConstants;
import com.huangrx.provider.exception.ApiException;
import com.huangrx.provider.model.entity.MyUser;
import com.huangrx.provider.service.RedisService;
import com.huangrx.provider.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author    hrenxiang
 * @since     2022/6/11 15:21
 */
public class SmsCodeFilter extends OncePerRequestFilter {

    @Resource
    private RedisService redisService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equalsIgnoreCase("/login/mobile", httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "get")) {
            try {
                if (validateSmsCode(httpServletRequest)) {
                    String mobile = httpServletRequest.getParameter("mobile");
                    String inputCode = httpServletRequest.getParameter("code");
                    Map<String, String> param = new HashMap<>();
                    param.put("mobile", mobile);
                    param.put("code", inputCode);
                    //jwtTokenUtil.generateToken(param);
                }

            } catch (ApiException e) {
                throw new ApiException(e.getMessage(), e.getCause());
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean validateSmsCode(HttpServletRequest httpServletRequest) throws ApiException {

        String mobile = httpServletRequest.getParameter("mobile");
        String inputCode = httpServletRequest.getParameter("code");

        if (StringUtils.isEmpty(mobile)) {
            throw new ApiException("手机号不能为空");
        }

        String code = redisService.get(CacheConstants.generateKey(CacheConstants.TEST_KEY, mobile)).toString();

        if (StringUtils.isBlank(code)) {
            throw new ApiException("验证码不能为空！");
        }

        Long expire = redisService.getExpire(CacheConstants.generateKey(CacheConstants.TEST_KEY, mobile));

        if (expire <= 0) {
            throw new ApiException("验证码已过期，请重新发送！");
        }
        if (!StringUtils.equalsIgnoreCase(code, inputCode)) {
            throw new ApiException("验证码不正确！");
        }

        return Boolean.TRUE;
    }
}