package com.huangrx.mybatisplus.service.impl;

import com.huangrx.mybatisplus.model.entity.User;
import com.huangrx.mybatisplus.mapper.primary.UserMapper;
import com.huangrx.mybatisplus.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author huangrx
 * @since 2022-04-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
