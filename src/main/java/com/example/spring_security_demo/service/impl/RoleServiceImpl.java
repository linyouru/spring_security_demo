package com.example.spring_security_demo.service.impl;

import com.example.spring_security_demo.entity.Role;
import com.example.spring_security_demo.dao.RoleDao;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.spring_security_demo.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

}
