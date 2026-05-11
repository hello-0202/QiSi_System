package com.sc.qisi_system.module.websocket.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * WebSocket 消息通信控制器
 * 功能: 处理客户端心跳、在线用户管理、在线状态查询等WebSocket通信操作
 */
@RequestMapping("/api/websocket")
@RequiredArgsConstructor
@RestController
@Slf4j
public class WebSocketController {


    private final WebSocketService webSocketService;
    private final RedisService redisService;


    /**
     * WebSocket心跳检测接口
     * 角色: 认领者 发布者 管理员
     *
     * @param accessor 消息头访问器
     * @return 心跳响应 pong
     */
    @MessageMapping("/ping")
    public String ping(SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        webSocketService.handleHeartBeat(sessionId);
        return "pong";
    }


    /**
     * 获取在线用户列表(广播)接口
     * 角色: 认领者 发布者 管理员
     *
     * @param accessor 消息头访问器
     */
    @MessageMapping("/user/online/list")
    public void getUserOnlineList(SimpMessageHeaderAccessor accessor) {
        Long userId = redisService.getUserId(accessor.getSessionId());
        if (userId == null) return;
        webSocketService.broadcastOnlineUserList();
    }


    /**
     * 查询指定用户是否在线接口
     * 角色: 认领者 发布者 管理员
     *
     * @param userId 用户ID
     * @return 用户在线状态
     */
    @GetMapping("/isOnline")
    public Result isOnline(@RequestParam Long userId) {
        return Result.success(webSocketService.isOnline(userId));
    }
}