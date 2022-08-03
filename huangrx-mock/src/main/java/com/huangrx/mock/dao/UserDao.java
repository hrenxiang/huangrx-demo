package com.huangrx.mock.dao;

import com.huangrx.mock.po.User;

/**
 * @author hrenxiang
 * @since 2022-07-28 14:10
 */
public interface UserDao {
    User getUserById(Integer id);

    Integer insertUser(User user);
}
