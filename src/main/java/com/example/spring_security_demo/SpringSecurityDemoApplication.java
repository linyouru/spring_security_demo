package com.example.spring_security_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.spring_security_demo.dao")   //映射mybatis的mapper接口所在的包
//@EnableScheduling //开启对定时任务的支持
@SpringBootApplication
public class SpringSecurityDemoApplication {
//test push
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
        System.out.println("================项目启动完成=================");
    }

     /*@Override
    protected void configurePathMatch(PathMatchConfigurer configurer) {
        //设置是否是后缀模式匹配，如“/user”是否匹配/user.*，默认真即匹配；
        configurer.setUseSuffixPatternMatch(false);
        //设置是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”，默认真即匹配；
        configurer.setUseTrailingSlashMatch(true);
    }*/
}
