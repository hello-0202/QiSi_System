package com.sc.qisi_system.module.websocket.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController("/api/websocket")
@Slf4j
public class WebSocketController {


    private final WebSocketService webSocketService;
    private final RedisService redisService;


    /**
     * 心跳
     */
    @MessageMapping("/ping")
    public String ping(SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        webSocketService.handleHeartBeat(sessionId);
        return "pong";
    }


    /**
     * 获取在线用户列表 - 广播
     */
    @MessageMapping("/user/online/list")
    public void getUserOnlineList(SimpMessageHeaderAccessor accessor) {
        Long userId = redisService.getUserId(accessor.getSessionId());
        if (userId == null) return;
        webSocketService.broadcastOnlineUserList();
    }


    /**
     * 查询指定用户是否在线接口
     *
     * @param userId 用户id
     * @return bool状态
     */
    @GetMapping("/isOnline")
    public Result isOnline(@RequestParam Long userId) {
        return Result.success(webSocketService.isOnline(userId));
    }

}