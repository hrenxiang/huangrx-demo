package com.huangrx.service.impl;

import com.huangrx.entity.User;
import com.huangrx.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@DubboService
public class UserServiceImpl implements UserService {

    @Override
    public List<User> getList() {
        List<User> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setName("张三" + i);
            user.setPwd("123" + i);
            list.add(user);
        }
        return list;
    }
}