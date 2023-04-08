package com.waltwang.maivc.security;

// 导入所需的类和包

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

// 使用@Component注解，使得JwtService类能够作为Spring组件被自动检测和注入到其他组件中
@Slf4j
@Component
public class JwtService {
    // 定义JWT令牌的过期时间，以毫秒为单位。这里设置为86400000毫秒，即24小时
    static final long EXPIRATIONTIME = 7200000;
    // 定义JWT令牌的前缀，通常在Authorization请求头中使用
    static final String PREFIX = "Bearer";
    // 生成一个用于签名和验证JWT令牌的密钥。在生产环境中，密钥应从应用程序配置文件中读取
    // static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    // 生成JWT令牌的方法，接收一个用户名作为参数
    public String getToken(String username) {
        String token = Jwts.builder()
                .setSubject(username) // 设置令牌的主题，即用户名
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME)) // 设置令牌的过期时间
                .signWith(getSigningKey()) // 使用密钥对令牌进行签名
                .compact(); // 构建并压缩令牌
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            // 如果令牌未过期且具有有效签名，则返回true
            return true;
        } catch (ExpiredJwtException e) {
            log.error("validateToken JWT has expired.", e);
            // 令牌已过期
            return false;
        } catch (JwtException e) {
            log.error("validateToken Invalid JWT token.", e);
            // 令牌签名无效或其他问题
            return false;
        }
    }

    // 从请求的Authorization头部获取JWT令牌，并解析令牌以获取用户名
    public String getAuthUser(HttpServletRequest request) {
        // 从请求头中获取Authorization字段的值
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            // 解析令牌并获取用户名
            try {
                String user = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey()) // 设置解析时使用的签名密钥
                        .build()
                        .parseClaimsJws(token.replace(PREFIX, "")) // 移除令牌前缀并进行解析
                        .getBody()
                        .getSubject(); // 获取令牌主题（用户名）
                return user;
            } catch (SignatureException e) {
                // To avoid printing the stack trace, you can remove the exception parameter from the log.error() method call. Instead, you can log just the error message.
                log.error("getAuthUser JWT signature does not match. JWT should not be trusted.");
            } catch (ExpiredJwtException e) {
                log.error("getAuthUser JWT has expired.");
            } catch (JwtException e) {
                log.error("getAuthUser Invalid JWT token.");
            }
        }
        return null;
    }
}