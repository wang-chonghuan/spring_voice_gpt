package com.waltwang.maivc.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 应用说明：
 访问 http://localhost:8080/chat/healthcheck
 返回 Error: Full authentication is required to access this resource
 原因是无认证时只允许访问 http://localhost:8080/login

 POST http://localhost:8080/login
 raw.JSON {"username":"user", "password":"1"}
 返回200 OK, body: 1
 headers看Authorization字段：Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjgwOTAxODc0fQ.CA15bpG95yayhS0tbb-TMKYHmBO_ua3XmN_xGgJj6Eo
 */

// 使用 @Configuration 和 @EnableWebSecurity 注解定义这个类作为配置类，并启用 Spring Security 的 Web 安全支持。
// 继承 WebSecurityConfigurerAdapter 类以自定义 Spring Security 配置。
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private AuthEntryPoint exceptionHandler;

    /**
     * 不能空实现这个方法，否则所有端口都不会要求验证
     * 通过 configure(HttpSecurity http) 方法配置 Spring Security。在这个方法中：
     * 禁用 CSRF 保护。
     * 启用 CORS（跨域资源共享）。
     * 禁用 Spring Security 的会话管理，使其无状态（即不创建会话）。
     * 允许未经身份验证的用户通过 POST 请求访问 /login 端点。
     * 要求访问其他所有端点的请求进行身份验证。
     * 处理无权限的异常（使用 AuthEntryPoint）。
     * 增加身份验证过滤器，在 UsernamePasswordAuthenticationFilter 之前添加自定义的 AuthenticationFilter。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // POST request to /login endpoint is not secured
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // All other requests are secured
                .anyRequest().authenticated()
                // 处理无权限的异常
                .and().exceptionHandling().authenticationEntryPoint(exceptionHandler)
                // 增加验证过滤器
                .and().addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 使用加密方式（如BCrypt）来存储用户密码
        //auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        // 明文保存密码
        auth.userDetailsService(userDetailsService).passwordEncoder(new NoOpPasswordEncoder());
    }

    // 注册 AuthenticationManager Bean，以便在其他地方（如 LoginController）使用。
    // 必须在WebSecurityConfigurerAdapter子类添加这个方法，把AuthenticationManager注册成一个Bean，LoginController才能使用
    // 否则会报错 Consider defining a bean of type 'org.springframework.security.authentication.AuthenticationManager' in your configuration
    // We have also injected AuthenticationManager into the LoginController class, therefore we have to add the following code to the SecurityConfig class:
    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    // 没有编码器会导致输完密码后台报错，前台不会跳转到目标页面
    // java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
    //这个错误表示Spring Security期望一个密码编码器（PasswordEncoder），但由于我们在前面的示例中删除了密码编码器，它找不到相应的编码器。为了解决这个问题，我们需要为Spring Security提供一个不执行任何加密的密码编码器。
    public static class NoOpPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword);
        }
    }

    // 处理跨域问题
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(false);
        // 添加 Authorization 到允许暴露的 header 列表中
        config.setExposedHeaders(Arrays.asList("Authorization"));

        config.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
