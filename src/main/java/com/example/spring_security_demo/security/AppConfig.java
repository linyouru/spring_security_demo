package com.example.spring_security_demo.security;

import com.example.spring_security_demo.SpringSecurityDemoApplication;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * 将配置springSecurity的配置类加载到spring容器中
 * @ClassName AppConfig
 * @Description TODO
 * @Author LYR
 * @Date 2019/3/26 18:11
 * @Version 1.0
 **/
public class AppConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        //2.1创建AnnotationConfigWebApplicationContext
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        //2.2spring的配置类  springSecurity的配置类
        springContext.register(SpringSecurityDemoApplication.class,SpringSecurityConfig.class);
    }
}
