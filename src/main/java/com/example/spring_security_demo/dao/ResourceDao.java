package com.example.spring_security_demo.dao;

import com.example.spring_security_demo.entity.Resource;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyr123
 * @since 2019-03-28
 */
public interface ResourceDao extends BaseMapper<Resource> {

    List<Resource> getAll();

}
