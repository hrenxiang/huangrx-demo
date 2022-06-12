package com.huangrx.provider.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huangrx.provider.exception.ApiException;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private ClientDetailsService clientDetailsService;
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Value("${authorization.tokenHeader}")
    private String authorizationTokenHeader;

    @Value("${authorization.tokenHead}")
    private String authorizationTokenHead;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 1. 从请求头中获取 ClientId
        String header = request.getHeader(authorizationTokenHeader);
        if (header == null || !header.startsWith(authorizationTokenHead)) {
            throw new ApiException("请求头中无client信息");
        }

        String[] tokens = this.extractAndDecodeHeader(header);
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        TokenRequest tokenRequest = null;

        // 2. 通过 ClientDetailsService 获取 ClientDetails
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        // 3. 校验 ClientId和 ClientSecret的正确性
        if (clientDetails == null) {
            throw new ApiException("clientId:" + clientId + "对应的信息不存在");
        }
        //else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
        else if (passwordEncoder.matches(clientDetails.getClientSecret() , clientSecret)) {
            throw new ApiException("clientSecret不正确");
        } else {
            // 4. 通过 TokenRequest构造器生成 TokenRequest
            tokenRequest = new TokenRequest(new HashMap<>(5), clientId, clientDetails.getScope(), "custom");
        }

        // 5. 通过 TokenRequest的 createOAuth2Request方法获取 OAuth2Request
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        // 6. 通过 Authentication和 OAuth2Request构造出 OAuth2Authentication
        OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        // 7. 通过 AuthorizationServerTokenServices 生成 OAuth2AccessToken
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(auth2Authentication);

        // 8. 返回 Token
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(token));
    }

    private String[] extractAndDecodeHeader(String header) {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }
}