package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.LogoutRequest;

import java.util.Map;


public interface RedisService {


    String generateRefreshToken(Long userId);


    Map<String, String> refreshAccessToken(String refreshToken);


    void logout(LogoutRequest logoutRequest);


    boolean isTokenBlacklisted(String accessToken);


    void saveMapping(Long userId, String sessionId);


    void removeMapping(Long userId, String sessionId);


    boolean  hasValidOldSession(Long userId, String newSessionId);


    void handleUserDisconnect(String sessionId);


    boolean isOnline(Long userId);


    void renewMapping(Long userId, String sessionId);


    String getSessionId(Long userId);


    Long getUserId(String sessionId);


    /**
     * 获取在线用户的 userId-sessionId 映射
     *
     * @return Map<userId, sessionId>
     */
    Map<Long, String> getAllOnlineUserSessionMap();
}

