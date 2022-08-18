package com.huangrx.controller;

import com.huangrx.entity.User;
import com.huangrx.service.PersonService;
import com.huangrx.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author        hrenxiang
 * @since         2022-08-18 15:09:00
 */
@RestController
public class TestController {

    @DubboReference
    private UserService userService;

    @DubboReference
    private PersonService personService;

    @GetMapping("/test")
    public List<User> test() {
        return userService.getList();
    }

    @GetMapping("/test2")
    public String test2() {
        return personService.sayCool();
    }
}