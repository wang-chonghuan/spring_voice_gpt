package com.waltwang.maivc.security;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * AuthEntryPoint 类：身份验证入口点
 *
 * 实现 AuthenticationEntryPoint 接口，该接口用于处理身份验证异常。
 * 重写 commence() 方法，该方法在身份验证失败时被调用。具体来说：
 * 将 HTTP 响应的状态码设置为 HttpServletResponse.SC_UNAUTHORIZED（401 Unauthorized）。
 * 将 HTTP 响应的内容类型设置为 JSON。
 * 获取一个 PrintWriter 对象（response.getWriter()），用于向响应中写入数据。
 * 使用 PrintWriter 将错误信息（来自 authException.getMessage()）写入 HTTP 响应。
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.println("Error: " + authException.getMessage());
    }
}