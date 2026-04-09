package com.sc.qisi_system.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     *  获取 Token 有效期（毫秒）
     */
    @Getter
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * 生成 Token
     * @param userId 用户ID
     * @param username 账号
     * @param userType 用户类型
     * @return JWT Token
     */
    public String generateToken(Long userId, String username, Integer userType) {
        Date now = new Date();

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("userType", userType)
                .issuedAt(now)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 从 Token 提取 userId
     */
    public Long getUserIdFromToken(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    /**
     * 从 Token 提取 username
     */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 从 Token 提取 userType
     */
    public Integer getUserTypeFromToken(String token) {
        return parseClaims(token).get("userType", Integer.class);
    }

    /**
     * 验证 Token 是否有效（返回 boolean，方便业务调用）
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 统一解析 Token 载荷（私有公共方法）
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 生成签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}