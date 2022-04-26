package com.huangrx.security.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * 存放模拟的用户数据
 *
 * @author        hrenxiang
 * @since         2022/4/26 11:11 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser implements Serializable {
    private static final long serialVersionUID = 3497935890426858541L;

    private String username;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked= true;

    private boolean credentialsNonExpired= true;

    private boolean enabled= true;

}