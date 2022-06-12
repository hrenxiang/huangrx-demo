package com.huangrx.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author    hrenxiang
 * @since     2022/6/9 16:00
 */
@RestController
@Slf4j
public class ApiController {

    @GetMapping("/login-success")
    public String loginSuccess(){
        return " 登录成功";
    }

    @GetMapping("index")
    public Object index(Authentication authentication){
        return authentication;
    }

}
