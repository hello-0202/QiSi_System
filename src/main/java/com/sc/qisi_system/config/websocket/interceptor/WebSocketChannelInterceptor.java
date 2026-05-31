package com.sc.qisi_system.config.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        // 打印 STOMP 命令 + 目标地址
        log.info("[STOMP] 命令: {}, 目标: {}", command, accessor.getDestination());

        // 打印 Authorization（如果有）
        String auth = accessor.getFirstNativeHeader("Authorization");
        if (auth != null) {
            log.info("[STOMP] Authorization: {}", auth.substring(0, Math.min(auth.length(), 20)) + "...");
        }

        // 打印消息体（只在 SEND 时打印，避免日志刷屏）
        if (command == StompCommand.SEND) {
            log.info("[STOMP] 消息体: {}", new String((byte[]) message.getPayload()));
        }

        return message;
    }
}
