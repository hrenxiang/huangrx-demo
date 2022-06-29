package com.huangrx.provider.service.impl;

import com.huangrx.provider.model.entity.MyUser;
import com.huangrx.provider.service.OauthAccountService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author    hrenxiang
 * @since     2022/6/9 14:51
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private OauthAccountService oauthAccountService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        //// 校验参数合法性
        //if (StringUtils.isEmpty(name)) {
        //    throw new ApiException("用户名为空");
        //}
        //
        //OauthAccount oauthAccount = oauthAccountService.loadUserByUsername(name);
        //
        //// 检查用户是否存在
        //if (oauthAccount == null || !oauthAccount.getAccountNonDeleted()) {
        //    throw new ApiException("用户不存在");
        //}
        //
        //// 检查用户状态
        //if (!oauthAccount.getAccountNonLocked()) {
        //    throw new ApiException("用户已被锁定");
        //}
        //
        //// 授权
        //List<GrantedAuthority> authorities = new ArrayList<>();
        //return new OauthAccountUserDetail(oauthAccount, authorities);

        MyUser user = new MyUser();
        user.setUserName(name);
        user.setPassword(this.passwordEncoder.encode("123456"));
        return new User(name, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

    }
}
