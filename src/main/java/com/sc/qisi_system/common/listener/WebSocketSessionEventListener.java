package com.sc.qisi_system.common.listener;


import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.RedisSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@RequiredArgsConstructor
@Component
public class WebSocketSessionEventListener {

    private final RedisSessionUtils redisSessionUtils;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();

        if (principal == null) return;

        Long userId = Long.parseLong(principal.getName());
        String newSessionId = accessor.getSessionId();

        // 检查是否有旧 session
        String oldSessionId = redisSessionUtils.getSessionId(userId);
        if (oldSessionId != null && !oldSessionId.equals(newSessionId)) {

            kickOutOldSession(userId, oldSessionId);
        }

        redisSessionUtils.saveMapping(userId, newSessionId);
    }

    // 连接断开时触发
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = accessor.getSessionId();
        Long userId = redisSessionUtils.getUserId(sessionId);

        if (userId != null) {

            String currentSessionId = redisSessionUtils.getSessionId(userId);
            if (sessionId != null && sessionId.equals(currentSessionId)) {
                redisSessionUtils.removeMapping(userId, sessionId);
            }
        }

    }

    // 踢出旧连接的逻辑
    private void kickOutOldSession(Long userId, String oldSessionId) {

        // 1.发送业务通知（旧客户端会收到消息，可提示后主动断开）
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/kick",
                ResultCode.ACCOUNT_LOGIN_OTHER_DEVICE.getMessage()
        );

        // 2.删除旧的 Redis 映射
        redisSessionUtils.removeMapping(userId, oldSessionId);
    }
}
