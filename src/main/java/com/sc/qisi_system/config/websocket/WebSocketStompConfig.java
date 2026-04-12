package com.sc.qisi_system.config.websocket;

import com.sc.qisi_system.config.websocket.interceptor.WebSocketHandshakeInterceptor;
import com.sc.qisi_system.config.websocket.interceptor.WebSocketChannelInterceptor;
import com.sc.qisi_system.config.websocket.handler.StompMessageErrorHandler;
import com.sc.qisi_system.config.websocket.handler.StompPrincipalHandshakeHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.nio.charset.StandardCharsets;
import java.util.List;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
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


    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new StringMessageConverter(StandardCharsets.UTF_8));
        return false; // 保留默认转换器（包括 JSON）
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/user/online")
                .addInterceptors(webSocketHandshakeInterceptor) // 👈 加这行
                .setAllowedOriginPatterns("**")
                .setHandshakeHandler(stompPrincipalHandshakeHandler);
        registry.setErrorHandler(stompMessageErrorHandler);
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketChannelInterceptor);
    }


    @Override
    public void configureClientOutboundChannel(@NotNull ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientOutboundChannel(registration);
    }


    @Override
    public void configureWebSocketTransport(@NotNull WebSocketTransportRegistration registry) {
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }


}
