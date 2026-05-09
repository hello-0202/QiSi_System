package com.sc.qisi_system.module.websocket.service.impl;

import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@RequiredArgsConstructor
@Service
public class WebSocketServiceImpl implements WebSocketService {


    private final RedisService redisService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    private static final String ONLINE_USER_TOPIC = "/topic/online/userList";


    @Override
    public void handleHeartBeat(String sessionId) {
        Long userId = redisService.getUserId(sessionId);
        if (userId != null) {
            redisService.renewMapping(userId, sessionId);
        }
    }


    @Override
    public void broadcastOnlineUserList() {
        Map<Long, String> allOnlineMap = redisService.getAllOnlineUserSessionMap();
        simpMessagingTemplate.convertAndSend(ONLINE_USER_TOPIC, allOnlineMap);
    }


    @Override
    public boolean isOnline(Long userId) {
        return redisService.isOnline(userId);
    }
}