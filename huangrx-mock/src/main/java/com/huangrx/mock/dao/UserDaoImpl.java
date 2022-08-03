package com.huangrx.mock.dao;

import com.huangrx.mock.po.User;
import org.springframework.stereotype.Service;

/**
 * @author hrenxiang
 * @since 2022-07-28 14:11
 */
@Service
public class UserDaoImpl implements UserDao {
    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public Integer insertUser(User user) {
        return null;
    }
}
