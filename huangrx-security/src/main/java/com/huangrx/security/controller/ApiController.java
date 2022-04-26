package com.huangrx.security.controller;

import com.huangrx.security.domain.BaseResponse;
import com.huangrx.security.model.po.MyUser;
import com.huangrx.security.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器
 *
 * @author hrenxiang
 * @since 2022-04-26 11:21 AM
 */
@Slf4j
@RestController
public class ApiController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public ApiController(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public BaseResponse<String> login(@RequestParam("username") String username) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        return BaseResponse.success(jwtTokenUtil.generateToken(userDetails));
    }

    @GetMapping("/get")
    @PreAuthorize(value = "hasAuthority('admin')")
    public BaseResponse<String> get() {
        return BaseResponse.success("huangrx----- 你真帅！！！");
    }
}
