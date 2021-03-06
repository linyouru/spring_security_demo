package com.example.spring_security_demo.dao;

import com.example.spring_security_demo.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyr123
 * @since 2019-03-27
 */
public interface UserDao extends BaseMapper<User> {

    User findUserbyName(String name);

}
