package com.example.spring_security_demo.service;

import com.example.spring_security_demo.entity.Resource;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyr123
 * @since 2019-03-28
 */
public interface ResourceService extends IService<Resource> {

    List<Resource> getAll();
}
