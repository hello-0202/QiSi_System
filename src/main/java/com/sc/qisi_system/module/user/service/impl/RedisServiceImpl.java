package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.JwtTokenProvider;
import com.sc.qisi_system.module.user.dto.LogoutRequest;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.mapper.SysUserMapper;
import com.sc.qisi_system.module.user.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final SysUserMapper sysUserMapper;

    @Value("${token.refresh-token-ttl}")
    private long refreshTokenTtl;

    @Value("${session.mapping-ttl-hours:1}")
    private long sessionMappingTtlHours;

    // Redis Key 前缀
    private static final String RT_PREFIX = "refresh-token:rt:";
    private static final String USER_RT_PREFIX = "refresh-token:user:";

    private static final String USER_TO_SESSION = "user:session:";
    private static final String SESSION_TO_USER = "session:user:";

    private static final String JWT_BLACKLIST_PREFIX = "jwt:blacklist:";


    @Override
    public String generateRefreshToken(Long userId) {
        String oldRefreshToken = stringRedisTemplate.opsForValue().get(USER_RT_PREFIX + userId);

        if (oldRefreshToken != null) {
            stringRedisTemplate.delete(RT_PREFIX + oldRefreshToken);
        }

        String refreshToken = UUID.randomUUID().toString();

        stringRedisTemplate.opsForValue().set(RT_PREFIX + refreshToken,
                String.valueOf(userId), refreshTokenTtl, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(USER_RT_PREFIX + userId,
                refreshToken, refreshTokenTtl, TimeUnit.HOURS);

        return refreshToken;
    }


    /**
     * 使用 Refresh Token 刷新 Access Token
     */
    @Override
    public Map<String, String> refreshAccessToken(String refreshToken) {

        String userId = stringRedisTemplate.opsForValue().get(RT_PREFIX + refreshToken);

        if (userId == null) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getId, userId)
                .select(SysUser::getId, SysUser::getUsername, SysUser::getUserType);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(sysUser.getId(), sysUser.getUsername(), sysUser.getUserType());

        revokeRefreshToken(userId, refreshToken);
        String newRefreshToken = generateRefreshToken(sysUser.getId());

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", newAccessToken);
        map.put("refreshToken", newRefreshToken);
        return map;
    }


    @Override
    public void logout(LogoutRequest logoutRequest) {

        revokeRefreshToken(logoutRequest.getUserId(), logoutRequest.getRefreshToken());

        if (logoutRequest.getAccessToken() != null && jwtTokenProvider.isTokenValid(logoutRequest.getAccessToken())) {
            long expireSeconds = jwtTokenProvider.getTokenRemainingTimeSeconds(logoutRequest.getAccessToken());
            if (expireSeconds > 0) {
                stringRedisTemplate.opsForValue().set(
                        JWT_BLACKLIST_PREFIX + logoutRequest.getAccessToken(),
                        "1",
                        expireSeconds,
                        TimeUnit.SECONDS
                );
            }
        }

        String sessionId = getSessionId(Long.valueOf(logoutRequest.getUserId()));
        if(sessionId != null) {
            removeMapping(Long.valueOf(logoutRequest.getUserId()),sessionId);
        }
    }


    @Override
    public boolean isTokenBlacklisted(String accessToken) {

        String blacklistKey = JWT_BLACKLIST_PREFIX + accessToken;
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(blacklistKey));

    }


    /**
     * 处理用户连接：顶号踢人 + 保存session
     */
    @Override
    public boolean hasValidOldSession(Long userId, String newSessionId) {
        // 1. 查旧session
        String oldSessionId = getSessionId(userId);

        // 2. 有旧session → 踢人
        return oldSessionId != null && !oldSessionId.equals(newSessionId);
    }


    /**
     * 处理用户断开：清理Redis
     */
    @Override
    public void handleUserDisconnect(String sessionId) {
        Long userId = getUserId(sessionId);
        if (userId == null) return;
        String currentSessionId = getSessionId(userId);
        if (sessionId.equals(currentSessionId)) {
            removeMapping(userId, sessionId);
        }
    }


    @Override
    public void removeMapping(Long userId, String sessionId) {
        stringRedisTemplate.delete(USER_TO_SESSION + userId);
        stringRedisTemplate.delete(SESSION_TO_USER + sessionId);
    }


    @Override
    public void saveMapping(Long userId, String sessionId) {
        stringRedisTemplate.opsForValue().set(USER_TO_SESSION + userId, sessionId, sessionMappingTtlHours, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(SESSION_TO_USER + sessionId, String.valueOf(userId), sessionMappingTtlHours, TimeUnit.HOURS);
    }


    @Override
    public boolean isOnline(Long userId) {
        return stringRedisTemplate.hasKey(USER_TO_SESSION + userId);
    }


    @Override
    public void renewMapping(Long userId, String sessionId) {
        stringRedisTemplate.expire(USER_TO_SESSION + userId, sessionMappingTtlHours, TimeUnit.HOURS);
        stringRedisTemplate.expire(SESSION_TO_USER + sessionId, sessionMappingTtlHours, TimeUnit.HOURS);
    }


    @Override
    public String getSessionId(Long userId) {
        return stringRedisTemplate.opsForValue().get(USER_TO_SESSION + userId);
    }


    @Override
    public Long getUserId(String sessionId) {
        String userIdStr = stringRedisTemplate.opsForValue().get(SESSION_TO_USER + sessionId);
        return userIdStr != null ? Long.valueOf(userIdStr) : null;
    }


    /**
     * 主动撤销 Refresh Token
     */
    private void revokeRefreshToken(String userId, String refreshToken) {
        stringRedisTemplate.delete(USER_RT_PREFIX + userId);
        stringRedisTemplate.delete(RT_PREFIX + refreshToken);
    }
}
