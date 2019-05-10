package com.example.spring_security_demo.security;

import com.example.spring_security_demo.dao.UserDao;
import com.example.spring_security_demo.dao.impl.ResourceDaoImpl;
import com.example.spring_security_demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserDao userDao;

    @Autowired
    private ResourceDaoImpl resourceDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDao.findUserbyName(userName);
        if(user==null)
            throw new UsernameNotFoundException("用户不存在");
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUser(user);
        //获取用户资源
        String id = user.getId();
        List<Map<String, Object>> resByUserId = resourceDao.getResByUserId(id);
        userDetails.setResource(resByUserId);
        return userDetails;

    }
}
