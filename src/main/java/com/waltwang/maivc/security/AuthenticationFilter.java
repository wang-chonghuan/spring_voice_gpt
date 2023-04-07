package com.waltwang.maivc.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * AuthenticationFilter 类：身份验证过滤器
 * <p>
 * 继承自 OncePerRequestFilter，这是一个抽象类，用于确保在一次请求中只执行一次过滤操作。
 * 注入 JwtService 依赖，用于处理 JWT（JSON Web Token）相关的操作。
 * 重写 doFilterInternal() 方法，该方法用于在请求处理过程中执行过滤操作。具体来说：
 * 从 HTTP 请求的 Authorization 头部获取 JWT 令牌。
 * 如果令牌存在，则验证令牌并从中获取用户信息。
 * 使用获取到的用户信息创建一个 Authentication 对象（UsernamePasswordAuthenticationToken 类型），
 * 并将其设置为当前的安全上下文中的身份验证信息（SecurityContextHolder.getContext().setAuthentication(authentication)）。
 * 调用 filterChain.doFilter(request, response) 方法以继续处理请求，将控制权传递给下一个过滤器。
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        // get token from the authorization header
        String jwsToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        // verify token and get user
        if (jwsToken != null) {
            // verify token and get user
            String user = jwtService.getAuthUser(request);
            // authenticate
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}