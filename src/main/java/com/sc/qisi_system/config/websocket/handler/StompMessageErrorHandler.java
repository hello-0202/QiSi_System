package com.sc.qisi_system.config.websocket.handler;

import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompMessageErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, @NotNull Throwable ex) {

        log.error("处理连接异常");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(clientMessage);
        Map<String, Object> map = accessor.getSessionAttributes();
        Result result;

        if (ex.getCause() instanceof BusinessException be) {

            log.warn("[业务异常] {},code={}，message={}",
                    map != null ? map.get("requestLog") : "未知请求",
                    be.getResultCode().getCode(),
                    be.getMessage());

            result = Result.error(be);

        } else {
            log.error("[WebSocket系统异常] 请求日志={}", ex.getMessage());
            result = Result.error(new BusinessException(ResultCode.SYSTEM_ERROR));
        }

        StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        errorAccessor.setSessionId(accessor.getSessionId());
        String json = JsonUtil.toJson(result);

        return MessageBuilder.createMessage(json.getBytes(), errorAccessor.getMessageHeaders());

    }
}
