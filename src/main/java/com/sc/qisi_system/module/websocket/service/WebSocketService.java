package com.sc.qisi_system.module.websocket.service;

/**
 * WebSocket 通信服务接口
 * 功能: 处理心跳检测、在线用户管理、在线状态广播、用户在线状态查询等业务逻辑
 */
public interface WebSocketService {


    /**
     * 处理心跳续期
     * 角色: 认领者 发布者 管理员
     *
     * @param sessionId WebSocket 会话ID
     */
    void handleHeartBeat(String sessionId);


    /**
     * 广播全量在线用户列表
     * 角色: 认领者 发布者 管理员
     */
    void broadcastOnlineUserList();


    /**
     * 查询指定用户是否在线
     * 角色: 认领者 发布者 管理员
     *
     * @param userId 用户ID
     * @return 用户在线状态
     */
    boolean isOnline(Long userId);
}