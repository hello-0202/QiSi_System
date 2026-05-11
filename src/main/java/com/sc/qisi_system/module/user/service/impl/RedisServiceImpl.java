package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.JwtTokenProvider;
import com.sc.qisi_system.module.user.dto.LogoutDTO;
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


/**
 * Redis 业务服务实现类
 */
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


    /**
     * 生成刷新令牌
     */
    @Override
    public String generateRefreshToken(Long userId) {
        // 1. 删除旧的 RefreshToken
        String oldRefreshToken = stringRedisTemplate.opsForValue().get(USER_RT_PREFIX + userId);

        if (oldRefreshToken != null) {
            stringRedisTemplate.delete(RT_PREFIX + oldRefreshToken);
        }

        // 2. 生成新的 RefreshToken
        String refreshToken = UUID.randomUUID().toString();

        // 3. 保存双向映射
        stringRedisTemplate.opsForValue().set(RT_PREFIX + refreshToken,
                String.valueOf(userId), refreshTokenTtl, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(USER_RT_PREFIX + userId,
                refreshToken, refreshTokenTtl, TimeUnit.HOURS);

        return refreshToken;
    }


    /**
     * 使用 RefreshToken 刷新 AccessToken
     */
    @Override
    public Map<String, String> refreshAccessToken(String refreshToken) {
        // 1. 校验 RefreshToken 是否有效
        String userId = stringRedisTemplate.opsForValue().get(RT_PREFIX + refreshToken);

        if (userId == null) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 2. 查询用户信息
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getId, userId)
                .select(SysUser::getId, SysUser::getUsername, SysUser::getUserType);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 3. 生成新 AccessToken
        String newAccessToken = jwtTokenProvider.generateAccessToken(sysUser.getId(), sysUser.getUsername(), sysUser.getUserType());

        // 4. 废除旧 Token，生成新 Token
        revokeRefreshToken(userId, refreshToken);
        String newRefreshToken = generateRefreshToken(sysUser.getId());

        // 5. 返回新令牌
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", newAccessToken);
        map.put("refreshToken", newRefreshToken);
        return map;
    }


    /**
     * 用户登出
     */
    @Override
    public void logout(LogoutDTO logoutDTO) {
        // 1. 撤销刷新令牌
        revokeRefreshToken(logoutDTO.getUserId(), logoutDTO.getRefreshToken());

        // 2. 将 AccessToken 加入黑名单
        if (logoutDTO.getAccessToken() != null && jwtTokenProvider.isTokenValid(logoutDTO.getAccessToken())) {
            long expireSeconds = jwtTokenProvider.getTokenRemainingTimeSeconds(logoutDTO.getAccessToken());
            if (expireSeconds > 0) {
                stringRedisTemplate.opsForValue().set(
                        JWT_BLACKLIST_PREFIX + logoutDTO.getAccessToken(),
                        "1",
                        expireSeconds,
                        TimeUnit.SECONDS
                );
            }
        }

        // 3. 清理会话映射
        String sessionId = getSessionId(Long.valueOf(logoutDTO.getUserId()));
        if(sessionId != null) {
            removeMapping(Long.valueOf(logoutDTO.getUserId()),sessionId);
        }
    }


    /**
     * 判断 Token 是否在黑名单
     */
    @Override
    public boolean isTokenBlacklisted(String accessToken) {
        String blacklistKey = JWT_BLACKLIST_PREFIX + accessToken;
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(blacklistKey));
    }


    /**
     * 检查是否存在有效旧会话（顶号逻辑）
     */
    @Override
    public boolean hasValidOldSession(Long userId, String newSessionId) {
        // 1. 查询旧 Session
        String oldSessionId = getSessionId(userId);

        // 2. 存在且不一致 → 需要踢人
        return oldSessionId != null && !oldSessionId.equals(newSessionId);
    }


    /**
     * 处理用户断开连接，清理 Redis
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


    /**
     * 删除用户与 Session 的双向映射
     */
    @Override
    public void removeMapping(Long userId, String sessionId) {
        stringRedisTemplate.delete(USER_TO_SESSION + userId);
        stringRedisTemplate.delete(SESSION_TO_USER + sessionId);
    }


    /**
     * 保存用户与 Session 双向映射
     */
    @Override
    public void saveMapping(Long userId, String sessionId) {
        stringRedisTemplate.opsForValue().set(USER_TO_SESSION + userId, sessionId, sessionMappingTtlHours, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(SESSION_TO_USER + sessionId, String.valueOf(userId), sessionMappingTtlHours, TimeUnit.HOURS);
    }


    /**
     * 判断用户是否在线
     */
    @Override
    public boolean isOnline(Long userId) {
        return stringRedisTemplate.hasKey(USER_TO_SESSION + userId);
    }


    /**
     * 续期会话有效期
     */
    @Override
    public void renewMapping(Long userId, String sessionId) {
        stringRedisTemplate.expire(USER_TO_SESSION + userId, sessionMappingTtlHours, TimeUnit.HOURS);
        stringRedisTemplate.expire(SESSION_TO_USER + sessionId, sessionMappingTtlHours, TimeUnit.HOURS);
    }


    /**
     * 根据用户ID获取SessionId
     */
    @Override
    public String getSessionId(Long userId) {
        return stringRedisTemplate.opsForValue().get(USER_TO_SESSION + userId);
    }


    /**
     * 根据SessionId获取用户ID
     */
    @Override
    public Long getUserId(String sessionId) {
        String userIdStr = stringRedisTemplate.opsForValue().get(SESSION_TO_USER + sessionId);
        return userIdStr != null ? Long.valueOf(userIdStr) : null;
    }


    /**
     * 获取所有在线用户的 userId <-> sessionId 映射
     */
    @Override
    public Map<Long, String> getAllOnlineUserSessionMap() {
        Map<Long, String> onlineMap = new HashMap<>();

        // 1. 获取所有 user:session:* 键
        String userSessionPattern = USER_TO_SESSION + "*";
        try {
            var keys = stringRedisTemplate.keys(userSessionPattern);
            if (keys.isEmpty()) {
                return onlineMap;
            }

            // 2. 遍历解析
            for (String key : keys) {
                String userIdStr = key.replace(USER_TO_SESSION, "");
                Long userId = Long.valueOf(userIdStr);

                String sessionId = stringRedisTemplate.opsForValue().get(key);
                if (sessionId != null) {
                    onlineMap.put(userId, sessionId);
                }
            }
        } catch (Exception ignored) {
        }
        return onlineMap;
    }


    /**
     * 主动撤销 RefreshToken
     */
    private void revokeRefreshToken(String userId, String refreshToken) {
        stringRedisTemplate.delete(USER_RT_PREFIX + userId);
        stringRedisTemplate.delete(RT_PREFIX + refreshToken);
    }
}