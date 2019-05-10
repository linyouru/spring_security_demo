package com.example.spring_security_demo.service.impl;

import com.example.spring_security_demo.entity.Resource;
import com.example.spring_security_demo.dao.ResourceDao;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.spring_security_demo.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyr123
 * @since 2019-03-28
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, Resource> implements ResourceService {

    @javax.annotation.Resource
    private ResourceDao resourceDao;

    @Override
    public List<Resource> getAll() {
        return resourceDao.getAll();
    }
}
