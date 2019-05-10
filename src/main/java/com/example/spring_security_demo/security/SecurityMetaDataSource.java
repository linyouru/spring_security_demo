package com.example.spring_security_demo.security;

import com.example.spring_security_demo.entity.Resource;
import com.example.spring_security_demo.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class SecurityMetaDataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private ResourceService resourceService;

    /**
     * Spring security元数据存储变量，存储资源与访问资源所需的权限，以有序链表式哈希表存储形式存储，单例模式下该变量会作为系统级变量存储
     */
    private LinkedHashMap<String,Collection<ConfigAttribute>> metaData = new LinkedHashMap<>();

    /**
     * Spring容器启动时自动调用的, 返回所有权限的集合. 一般把所有请求与权限的对应关系也要在这个方法里初始化, 保存在一个属性变量里.
     *
     * @Author LinYouRu
     * @Description //TODO
     * @Date 11:29 2019/3/28
     * @Param []
     * @return void
     **/
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        /*initMetaData();
        System.out.println("初始化元数据");
        Collection<Collection<ConfigAttribute>> values = metaData.values();
        Collection<ConfigAttribute> all=new ArrayList<>();
        for (Collection<ConfigAttribute> each:values){
            each.forEach(configAttribute -> {
                all.add(configAttribute);
            });
        }
        return all;*/
        return null;
    }

    /**
     * 从数据库初始化安全元数据:定义为public 为动态重载安全元数据做接口
     */
    @PostConstruct
    public void initMetaData(){
        List<Resource> list = resourceService.getAll();
        metaData=new LinkedHashMap<>();
        for (Resource resource:list){   //从数据库获取到全部资源，存储到metaData
            List<ConfigAttribute> attributes=new ArrayList<>();
            attributes.add(new SecurityConfig(resource.getResCode()));
            metaData.put(resource.getUrl(),attributes);
        }
        List<ConfigAttribute> base=new ArrayList<>();
        base.add(new SecurityConfig("AUTH_0"));
        metaData.put("/**",base);
    }

    /**
     * 当接收到一个http请求时, filterSecurityInterceptor会调用该方法. 参数object是一个包含url信息的HttpServletRequest实例.
     * 这个方法要返回请求该url所需要的所有权限集合.
     * @Author LinYouRu
     * @Description //TODO
     * @Date 11:30 2019/3/28
     * @Param [object]
     * @return java.util.Collection<org.springframework.security.access.ConfigAttribute>
     **/
    @Override
    public Collection<ConfigAttribute> getAttributes(Object filter) throws IllegalArgumentException {
        FilterInvocation filterInvocation= (FilterInvocation) filter;
        String requestUrl = filterInvocation.getRequestUrl();
        System.out.println("请求的Url："+requestUrl);
        Iterator<Map.Entry<String, Collection<ConfigAttribute>>> iterator = metaData.entrySet().iterator();
        Collection<ConfigAttribute> rs=new ArrayList<>();
        while (iterator.hasNext()){
            Map.Entry<String, Collection<ConfigAttribute>> next = iterator.next();
            String url = next.getKey();
            Collection<ConfigAttribute> value = next.getValue();
            RequestMatcher requestMatcher=new AntPathRequestMatcher(url);
            if (requestMatcher.matches(filterInvocation.getRequest())){
                rs = value;
                break;
            }
        }
        System.out.println("拦截认证权限为："+rs);
        return rs;
    }

    /**
     * 如果为真则说明支持当前格式类型,才会到上面的 getAttributes 方法中
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
