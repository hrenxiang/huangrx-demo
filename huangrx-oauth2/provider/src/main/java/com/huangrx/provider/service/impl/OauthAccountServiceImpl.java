package com.huangrx.provider.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huangrx.provider.mapper.OauthAccountMapper;
import com.huangrx.provider.model.entity.OauthAccount;
import com.huangrx.provider.service.OauthAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huangrx
 * @since 2022-06-08
 */
@Service
public class OauthAccountServiceImpl extends ServiceImpl<OauthAccountMapper, OauthAccount> implements OauthAccountService {

    @Resource
    private OauthAccountMapper oauthAccountMapper;

    @Override
    public OauthAccount loadUserByUsername(String name) {

        return oauthAccountMapper.selectOne(
                Wrappers.<OauthAccount>lambdaQuery()
                        .eq(OauthAccount::getUsername, name)
        );
    }
}
