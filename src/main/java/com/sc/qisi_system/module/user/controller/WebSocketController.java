package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.module.user.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class WebSocketController {

    private final RedisService redisService;


    /**
     * WebSocket心跳接口: 保活+续期在线状态
     */
    @MessageMapping("/ping")
    public String ping(SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        Long userId = redisService.getUserId(sessionId);
        if (userId != null) {
            redisService.renewMapping(userId, sessionId);
        }
        return "pong";
    }
}
