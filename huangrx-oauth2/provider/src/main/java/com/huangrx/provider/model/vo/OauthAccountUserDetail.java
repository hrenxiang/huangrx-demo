//package com.huangrx.provider.model.vo;
//
//import com.huangrx.provider.model.entity.OauthAccount;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Set;
//
///**
// * @author    hrenxiang
// * @since     2022/6/9 15:06
// */
//public class OauthAccountUserDetail implements UserDetails {
//
//    private OauthAccount oauthAccount;
//
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public OauthAccountUserDetail(OauthAccount oauthAccount, Collection<? extends GrantedAuthority> authorities) {
//        this.oauthAccount = oauthAccount;
//        this.authorities = authorities;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return oauthAccount.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return oauthAccount.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return oauthAccount.getAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return oauthAccount.getAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return oauthAccount.getCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return oauthAccount.getEnabled();
//    }
//}
