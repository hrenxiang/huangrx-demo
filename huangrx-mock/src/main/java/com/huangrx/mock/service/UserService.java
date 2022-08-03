package com.huangrx.mock.service;

import com.huangrx.mock.dao.UserDao;
import com.huangrx.mock.po.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author    hrenxiang
 * @since     2022/7/28 14:11
 */
@Component
public class UserService {
    
    @Resource
    private UserDao userDao;

    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    public Integer insertUser(User user) {
        return userDao.insertUser(user);
    }
}