package com.example.spring_security_demo.security;

import com.example.spring_security_demo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import java.util.*;

public class UserDetailsImpl implements UserDetails {

    @Resource
    private User user;

    /**
     * 权限管理相关字段
     */
    private List<Map<String,Object>> resourceList;
    private ArrayList<GrantedAuthority> authorities;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Map<String, Object>> getResource() {
        return resourceList;
    }

    public void setResource(List<Map<String, Object>> resourceList) {
        this.resourceList = resourceList;
    }

    /**
     * 获取用户所拥有的权限
     **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities==null){
            authorities= new ArrayList<>();
            if(resourceList!=null&&resourceList.size()>0){
                for (Map<String, Object> resource : resourceList) {
                    authorities.add(new SimpleGrantedAuthority(resource.get("res_code").toString()));
                }
            }
            authorities.add(new SimpleGrantedAuthority("AUTH_0"));
        }
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    //用户是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //用户是否被锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //认证是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //用户是否激活
    @Override
    public boolean isEnabled() {
        return true;
    }
}
