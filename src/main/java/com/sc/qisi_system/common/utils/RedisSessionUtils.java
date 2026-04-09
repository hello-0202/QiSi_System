package com.sc.qisi_system.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisSessionUtils {

    private final StringRedisTemplate redisTemplate;
    private static final String USER_TO_SESSION = "user:session:";
    private static final String SESSION_TO_USER = "session:user:";

    public void saveMapping(Long userId, String sessionId) {

        redisTemplate.opsForValue().set(USER_TO_SESSION + userId, sessionId, 12, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(SESSION_TO_USER + sessionId, String.valueOf(userId), 12, TimeUnit.HOURS);

    }

    public String getSessionId(Long userId) {

        return redisTemplate.opsForValue().get(USER_TO_SESSION + userId);

    }

    public Long getUserId(String sessionId) {

        String userIdStr = redisTemplate.opsForValue().get(SESSION_TO_USER + sessionId);
        return userIdStr != null ? Long.valueOf(userIdStr) : null;

    }

    public void removeMapping(Long userId, String sessionId) {

        redisTemplate.delete(USER_TO_SESSION + userId);
        redisTemplate.delete(SESSION_TO_USER + sessionId);

    }

    public boolean isOnline(Long userId) {

        return redisTemplate.hasKey(USER_TO_SESSION + userId);

    }


}
