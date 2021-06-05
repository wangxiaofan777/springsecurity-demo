package com.wxf.springsecuritydemo.security.config;

import com.wxf.springsecuritydemo.handler.MyAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Security配置
 */
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单提交
        http.formLogin()
                // 自定义入参
                .usernameParameter("username")
                .passwordParameter("password")
                // 表单登录页面
                .loginPage("/login.html")
                // 必须和表单提交的接口一样，会去自定义登录逻辑
                .loginProcessingUrl("/login")
                .successForwardUrl("/toMain")
//                .successForwardUrl("http://www.baidu.com")
                // 使用自定义处理器，不能与successForwardUrl共存
//                .successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                // 跳转失败
                .failureForwardUrl("/toErr")
//                .failureHandler(new MyAuthenticationFailureHandler("/error.html"))
        ;

        // 验证
        http.authorizeRequests()
                // 执行login.html不需要认证
                .antMatchers("/login.html").permitAll()
                // 执行 error.html不需要认证
                .antMatchers("/error.html").permitAll()
                // 方形静态资源
//                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // 相当于下面的mvcMatchers
//                .antMatchers("/servlet/demo").permitAll()
                // 配置Servlet
//                .mvcMatchers("/demo").servletPath("/servlet").permitAll()
                // 多权限访问
//                .antMatchers("/main1.html").hasAnyAuthority("admin", "ADMIN")
                // admin可以反问 严格区分大小写
//                .antMatchers("/main1.html").hasAuthority("admin")
                .antMatchers("/main1.html").hasAuthority("admin1")
                // 根据角色访问，严格区分大小写
//                .antMatchers("/main1.html").hasRole("abc")
                // 根据多角色访问，严格区分大小写
//                .antMatchers("/main1.html").hasAnyRole("abc")
                // 根据地址配置
//                .antMatchers("/main1.html").hasIpAddress("127.0.0.1")
                // 自定义access的访问控制
                .antMatchers("/main1.html").access("hasRole('abc')")

                // 自定义access方法
                .anyRequest().access("@myServiceImpl.hasPermission(reqquest,authentication)")
                // 所有请求都必须认证才能访问，必须登陆
                .anyRequest().authenticated();

        // 异常处理
        http.exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler());

        // 关闭跨站请求伪造
        http.csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
