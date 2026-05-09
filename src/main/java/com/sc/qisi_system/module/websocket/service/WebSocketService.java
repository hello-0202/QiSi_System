package com.sc.qisi_system.module.websocket.service;


public interface WebSocketService {


    /**
     * 心跳续期
     */
    void handleHeartBeat(String sessionId);


    /**
     * 广播全量在线用户列表
     */
    void broadcastOnlineUserList();


    /**
     * 查询指定用户是否在线
     *
     * @param userId 用户id
     * @return bool状态
     */
    boolean isOnline(Long userId);
}
