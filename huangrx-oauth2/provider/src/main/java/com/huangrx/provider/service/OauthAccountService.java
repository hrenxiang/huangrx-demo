package com.huangrx.provider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huangrx.provider.model.entity.OauthAccount;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huangrx
 * @since 2022-06-08
 */
public interface OauthAccountService extends IService<OauthAccount> {

    /**
     * 根据用户名加载用户信息
     * @param name 用户名
     * @return 用户信息
     */
    OauthAccount loadUserByUsername(String name);
}
