package com.sc.qisi_system.common.utils;

import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {


    @Value("${jwt.secret}")
    private String jwtSecret;


    /**
     * 获取 accessToken 有效期 (毫秒)
     */
    @Getter
    @Value("${jwt.access-token-ttl}")
    private long accessTokenTtl;


    /**
     * 生成 Token
     *
     * @param userId   用户ID
     * @param username 账号
     * @param userType 用户类型
     * @return JWT Token
     */
    public String generateAccessToken(Long userId, String username, Integer userType) {
        Date now = new Date();

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("userType", userType)
                .expiration(new Date(now.getTime() + accessTokenTtl))
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
    public void validateToken(String token) throws JwtException {
        parseClaims(token);
    }

    /**
     * 返回 boolean
     */
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * 获取 Token 剩余过期时间（单位：秒）
     * 存入 Redis 黑名单最常用
     */
    public long getTokenRemainingTimeSeconds(String token) {
        long remainingMs = getTokenRemainingTime(token);
        return remainingMs > 0 ? remainingMs / 1000 : -1;
    }

    /**
     * 获取 Token 剩余过期时间（单位：毫秒）
     * 如果已过期/无效，返回 -1
     */
    private long getTokenRemainingTime(String token) {
        try {
            Claims claims = parseClaims(token);
            Date expiration = claims.getExpiration();
            if (expiration == null) {
                return -1;
            }
            return expiration.getTime() - System.currentTimeMillis();
        } catch (JwtException | IllegalArgumentException e) {
            return -1;
        }
    }

    /**
     * 统一解析 Token 载荷（私有公共方法）
     */
    private Claims parseClaims(String token) {

        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        } catch (SignatureException e) {
            throw new BusinessException(ResultCode.TOKEN_SIGNATURE_ERROR);
        } catch (MalformedJwtException e) {
            throw new BusinessException(ResultCode.TOKEN_MALFORMED);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        return claims;
    }

    /**
     * 生成签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}