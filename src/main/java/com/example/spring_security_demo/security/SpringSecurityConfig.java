package com.example.spring_security_demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.添加@EnableWebSecurity  创建过滤器执行链给spring容器管理 springSecurityFilterChain
 * 2.继承WebSecurityConfigurerAdapter 目的是重写父类的配置方法 应用自己添加的配置
 * 3.注解@EnableGlobalMethodSecurity  开启注解拦截方法的请求
 *       prePostEnabled 设置在请求方法之前拦截auth方法，进行权限校验
 * @Author lyr
 * @Date 10:40 2019-03-04
 * @Param
 * @return
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //  启用方法级别的权限认证
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private SecurityMetaDataSource securityMetaDataSource;

    /**
     * configure(AuthenticationManagerBuilder auth)方法是用于配置
     * 权限验证登陆管理
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);

        /**
         * 自定义配置验证的service类 : 目的是查询数据库读取所有的用户权限
         * 采用密文验证  :passwordEncoder
         * 加密方式为      :getBCryptPasswordEncoder()
         */
        auth.userDetailsService(userDetailService).passwordEncoder(getBCryptPasswordEncoder());
    }



    /**
     * 将加密方式的配置对象送入容器
     */
    @Bean
    public BCryptPasswordEncoder  getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(4);
    }

    /**
     * 解决静态资源被拦截的问题
     * 将自定义FilterSecurityInterceptor加入过滤链后会导致permitAll()前配置的URL被拦截
     * 将URL添加到ignoring中不走过滤连即可解决
     **/
    @Override
    public void configure(WebSecurity web) throws Exception {
        //添加不需要认证的路径
        web.ignoring().antMatchers("/page/login.jsp", "/login/login","/page/index.jsp");
        super.configure(web);
    }

    /**
     * configure(HttpSecurity http) 用于请求回调拦截的配置
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //添加不需要认证的路径
//                .antMatchers("/page/login.jsp", "/login/login.do", "/page/test.jsp").permitAll()
                //任何请求需要认证
                .anyRequest().authenticated()
                //权限前缀不要给
                //.anyRequest().hasRole("ACCESS")
                //.anyRequest().hasAnyAuthority()
                //.anyRequest().fullyAuthenticated()
                //关闭csrf 关闭跨域的验证
                .and().csrf().disable()
                //自定义配置登陆页面
                .formLogin().loginPage("/page/login.jsp")
                //自定义登陆请求地址
                .loginProcessingUrl("/login/login.do").
                //验证成功的跳转页面
                        successForwardUrl("/page/index.jsp").
                //验证失败的跳转路径
                        failureForwardUrl("/page/fail.jsp")
                //退出的请求路径
                .and().logout().logoutUrl("/login/logout.do").
                //退出请求后销毁session
                        invalidateHttpSession(true).
                //退出成功跳转的页面
                        logoutSuccessUrl("/page/login.jsp")
                //权限不足跳转的路径
                .and().exceptionHandling().accessDeniedPage("/page/accessDenied.jsp")
                //登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
                /*.and().rememberMe().tokenValiditySeconds(1209600)*/; //记住用户的时效

        /**
         *  FilterSecurityInterceptor本身属于过滤器，不能在外面定义为@Bean，
         *  如果定义在外面，则这个过滤器会被独立加载到webContext中，导致请求会一直被这个过滤器拦截
         *  在方法内部定义，再加入到Springsecurity的过滤器链中，才会使它完整的生效
         */
        FilterSecurityInterceptor filterSecurityInterceptor=new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(securityMetaDataSource);
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        http.addFilterAt(filterSecurityInterceptor,FilterSecurityInterceptor.class);

    }

    /**
     * 定义决策管理器，这里可直接使用内置的AffirmativeBased选举器，
     * 如果需要，可自定义，继承AbstractAccessDecisionManager，实现decide方法即可
     * @return
     */
    @Bean
    public AccessDecisionManager affirmativeBased(){
        List<AccessDecisionVoter<? extends Object>> voters=new ArrayList<>();
        voters.add(roleVoter());
        System.out.println("正在创建决策管理器");
        return new AffirmativeBased(voters);
    }

    /**
     * 定义选举器
     * @return
     */
    @Bean
    public RoleVoter roleVoter(){
        //这里使用角色选举器
        RoleVoter voter=new RoleVoter();
        System.out.println("正在创建选举器");
        voter.setRolePrefix("AUTH_");
        System.out.println("已将角色选举器的前缀修改为AUTH_");
        return voter;
    }


    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }


}
