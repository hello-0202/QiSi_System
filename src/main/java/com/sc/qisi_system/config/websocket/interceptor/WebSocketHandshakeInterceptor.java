package com.sc.qisi_system.config.websocket.interceptor;

import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.JsonUtil;
import com.sc.qisi_system.common.utils.JwtTokenProvider;
import com.sc.qisi_system.common.utils.RequestUtils;
import com.sc.qisi_system.module.user.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final RequestUtils requestUtils;
    private final RedisService redisService;

    @Override
    public boolean beforeHandshake(
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response,
            @NotNull WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        attributes.put("requestLog", requestUtils.getWebSocketRequestLog(request));
        try {
            // 1. 获取 token
            String token = getToken(request);

            if(redisService.isTokenBlacklisted(token)) {
                throw new BusinessException(ResultCode.TOKEN_LOGGED_OUT);
            }
            // 2. 校验 token（你原来的逻辑）
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            attributes.put("userId", userId);

            return true;

        } catch (BusinessException e) {

            log.error("[WebSocket JWT 验证异常]{}, 异常: {}",
                    attributes.get("requestLog"),
                    e.getMessage());

            sendErrorMessage(response, e);
            return false;
        }
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {

    }

    private String getToken(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query == null) throw new BusinessException(ResultCode.NO_TOKEN);

        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && "token".equals(kv[0])) {
                return kv[1];
            }
        }
        throw new BusinessException(ResultCode.NO_TOKEN);
    }

    private void sendErrorMessage(ServerHttpResponse response, Throwable ex) {
        try {
            BusinessException be = (BusinessException) ex;
            Result result = Result.error(be);
            String json = JsonUtil.toJson(result);

            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

            try (OutputStream os = response.getBody()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (Exception ignored) {
        }
    }

}