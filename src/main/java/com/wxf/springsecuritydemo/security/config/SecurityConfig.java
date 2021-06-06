package com.wxf.springsecuritydemo.security.config;

import com.wxf.springsecuritydemo.handler.MyAccessDeniedHandler;
import com.wxf.springsecuritydemo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Security配置
 */
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
//                .anyRequest().access("@myServiceImpl.hasPermission(reqquest,authentication)")
                // 所有请求都必须认证才能访问，必须登陆
                .anyRequest().authenticated();

        // 记住我
        http.rememberMe()
                // 配置数据有
                .tokenRepository(persistentTokenRepository)
                // 设置参数
                .rememberMeParameter("rememberMe")
                // 超时时间
                .tokenValiditySeconds(60)
                // 设置自定义登录逻辑
                .userDetailsService(userDetailsService);

        // 异常处理
        http.exceptionHandling()
                .accessDeniedHandler(new MyAccessDeniedHandler());

        // 关闭跨站请求伪造
        http.csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 自动建表
        jdbcTokenRepository.setCreateTableOnStartup(true);
        // 配置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
