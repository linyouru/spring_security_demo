package com.example.spring_security_demo.service.impl;

import com.example.spring_security_demo.entity.User;
import com.example.spring_security_demo.dao.UserDao;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.spring_security_demo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyr123
 * @since 2019-03-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

}
