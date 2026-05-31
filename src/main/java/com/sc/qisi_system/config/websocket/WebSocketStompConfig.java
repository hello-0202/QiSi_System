package com.sc.qisi_system.config.websocket;

import com.sc.qisi_system.config.websocket.interceptor.WebSocketHandshakeInterceptor;
import com.sc.qisi_system.config.websocket.interceptor.WebSocketChannelInterceptor;
import com.sc.qisi_system.config.websocket.handler.StompMessageErrorHandler;
import com.sc.qisi_system.config.websocket.handler.StompPrincipalHandshakeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketChannelInterceptor webSocketChannelInterceptor;
    private final StompMessageErrorHandler stompMessageErrorHandler;
    private final StompPrincipalHandshakeHandler stompPrincipalHandshakeHandler;
    private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue", "/topic");
        config.setUserDestinationPrefix("/user");
    }

//    @Override
//    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
//        messageConverters.add(new StringMessageConverter(StandardCharsets.UTF_8));
//        return false;
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/user/online")
                .addInterceptors(webSocketHandshakeInterceptor)
                .setAllowedOriginPatterns("**")
                .setHandshakeHandler(stompPrincipalHandshakeHandler);
        registry.setErrorHandler(stompMessageErrorHandler);
    }

    // 入站：前端 → 后端（你原来的，不动）
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketChannelInterceptor);
    }

    // 出站：后端 → 前端（我帮你修正完整版）
    @Override
    public void configureClientOutboundChannel(@NotNull ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                log.info("[✅ 出站STOMP] 命令={}, 目标={}",
                        accessor.getCommand(), accessor.getDestination());
                return message;
            }
        });
    }

    @Override
    public void configureWebSocketTransport(@NotNull WebSocketTransportRegistration registry) {
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }
}