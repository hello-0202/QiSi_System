package com.sc.qisi_system.module.websocket.service;

public interface SendWebSocketService {
    void sendKickOffNotice(Long userId, String reason);

    void broadcastToAll(String message);
}
