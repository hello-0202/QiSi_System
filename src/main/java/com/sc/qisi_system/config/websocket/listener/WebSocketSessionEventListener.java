package com.sc.qisi_system.config.websocket.listener;

import com.sc.qisi_system.config.websocket.StompPrincipal;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.service.UserOnlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketSessionEventListener {

    private final RedisService redisService;
    private final UserOnlineService userOnlineService;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        log.info("进入连接后监听");

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        StompPrincipal principal = (StompPrincipal) accessor.getUser();
        if (principal == null) return;

        Long userId = Long.valueOf(principal.getName());
        String newSessionId = accessor.getSessionId();

        if(redisService.hasValidOldSession(userId, newSessionId)){
            userOnlineService.kickOutOldSession(userId, newSessionId);
        }

        // 3. 保存新session到Redis
        redisService.saveMapping(userId, newSessionId);
    }

    // 连接断开时触发
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        redisService.handleUserDisconnect(sessionId);

    }


}
