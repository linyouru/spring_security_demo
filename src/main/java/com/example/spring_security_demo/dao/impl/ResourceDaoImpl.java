package com.example.spring_security_demo.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * mybatis的关联查询太麻烦，直接用jdbcTemplate
 * @ClassName ResourceDaoImpl
 * @Description TODO
 * @Author LYR
 * @Date 2019/3/28 17:01
 * @Version 1.0
 **/
@Repository
public class ResourceDaoImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> getResByUserId(String userId){
        String sql = "SELECT * FROM sys_resource c LEFT JOIN ( SELECT b.res_id FROM sys_role_res b LEFT JOIN ( SELECT role_id FROM sys_user_role WHERE user_id = '"+userId+"' ) a ON a.role_id = b.role_id ) d ON c.id = d.res_id";
        return jdbcTemplate.queryForList(sql);
    }
}
