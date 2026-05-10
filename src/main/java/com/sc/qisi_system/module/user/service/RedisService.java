package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.LogoutDTO;
import java.util.Map;


/**
 * Redis 缓存业务服务接口
 * 处理用户令牌管理、在线状态维护、会话管理、用户登出等相关操作
 */
public interface RedisService {


    /**
     * 生成刷新令牌
     *
     * @param userId 用户ID
     * @return 刷新令牌
     */
    String generateRefreshToken(Long userId);


    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的令牌对
     */
    Map<String, String> refreshAccessToken(String refreshToken);


    /**
     * 用户退出登录
     *
     * @param logoutDTO 登出参数
     */
    void logout(LogoutDTO logoutDTO);


    /**
     * 判断令牌是否在黑名单中
     *
     * @param accessToken 访问令牌
     * @return 是否失效
     */
    boolean isTokenBlacklisted(String accessToken);


    /**
     * 保存用户ID与会话ID的映射
     *
     * @param userId    用户ID
     * @param sessionId 会话ID
     */
    void saveMapping(Long userId, String sessionId);


    /**
     * 移除用户ID与会话ID的映射
     *
     * @param userId    用户ID
     * @param sessionId 会话ID
     */
    void removeMapping(Long userId, String sessionId);


    /**
     * 检查用户是否存在有效旧会话
     *
     * @param userId      用户ID
     * @param newSessionId 新会话ID
     * @return 是否存在
     */
    boolean hasValidOldSession(Long userId, String newSessionId);

    /**
     * 处理用户断开连接
     *
     * @param sessionId 会话ID
     */
    void handleUserDisconnect(String sessionId);


    /**
     * 判断用户是否在线
     *
     * @param userId 用户ID
     * @return 在线状态
     */
    boolean isOnline(Long userId);


    /**
     * 续期用户会话映射
     *
     * @param userId    用户ID
     * @param sessionId 会话ID
     */
    void renewMapping(Long userId, String sessionId);


    /**
     * 根据用户ID获取会话ID
     *
     * @param userId 用户ID
     * @return 会话ID
     */
    String getSessionId(Long userId);


    /**
     * 根据会话ID获取用户ID
     *
     * @param sessionId 会话ID
     * @return 用户ID
     */
    Long getUserId(String sessionId);


    /**
     * 获取在线用户的 userId-sessionId 映射
     *
     * @return Map<userId, sessionId>
     */
    Map<Long, String> getAllOnlineUserSessionMap();
}