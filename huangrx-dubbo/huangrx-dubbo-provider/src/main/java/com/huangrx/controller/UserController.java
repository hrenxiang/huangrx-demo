package com.huangrx.controller;

import com.huangrx.entity.User;
import com.huangrx.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author        hrenxiang
 * @since         2022-08-18 14:48:52
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/get")
    public List<User> getList() {
        return userService.getList();
    }
}