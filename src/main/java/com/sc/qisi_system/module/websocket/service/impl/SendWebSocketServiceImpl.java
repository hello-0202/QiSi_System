package com.sc.qisi_system.module.websocket.service.impl;


import com.sc.qisi_system.module.websocket.service.SendWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class SendWebSocketServiceImpl implements SendWebSocketService {


    private final SimpMessagingTemplate simpMessagingTemplate;


    /**
     * 发送强制下线指令
     * 前端订阅地址：/user/queue/system/kick
     *
     * @param userId 被封禁的用户ID
     * @param reason 下线原因
     */
    @Override
    public void sendKickOffNotice(Long userId, String reason) {
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/system/kick",
                reason
        );
        log.info("已强制用户 {} 下线，原因：{}", userId, reason);
    }


    /**
     * 【全局广播】发送消息给 所有在线用户
     * 前端订阅：/topic/system/global
     */
    @Override
    public void broadcastToAll(String message) {
        simpMessagingTemplate.convertAndSend(
                "/topic/system/global",
                message
        );
        log.info("全局广播消息：{}", message);
    }
}
