package com.sc.qisi_system.module.websocket.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.websocket.dto.UserMessageDTO;
import com.sc.qisi_system.module.websocket.service.MessageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 聊天消息管理控制器
 * 包含：私信发送、会话管理、聊天记录、未读消息、已读标记
 */
@RequestMapping("/api/message")
@RequiredArgsConstructor
@Controller
public class MessageController {


    private final MessageService messageService;


    /**
     * 发送私人私信（WebSocket）
     */
    @MessageMapping("/send/private")
    public void sendPrivateMessage(UserMessageDTO userMessageDTO, SimpMessageHeaderAccessor accessor) {
        messageService.sendPrivateMessage(userMessageDTO, accessor);
    }


    /**
     * 创建 or 获取 单聊对话接口
     *
     * @param targetUserId 对方用户ID
     * @return 对话信息 sessionId
     */
    @GetMapping("/create-or-get")
    public Result createOrGetSession(@RequestParam Long targetUserId) {
        return Result.success(messageService.getOrCreateSession(SecurityUtils.getCurrentUserId(), targetUserId));
    }


    /**
     * 获取当前用户的会话列表接口
     *
     * @return 会话列表
     */
    @GetMapping("/chat/session/list")
    public Result getChatSessionList() {
        return Result.success(messageService.getChatSessionList(SecurityUtils.getCurrentUserId()));
    }


    /**
     * 获取指定会话的聊天记录接口
     *
     * @param sessionId 会话ID
     * @return 聊天记录列表
     */
    @GetMapping("/chat/history")
    public Result getChatHistory(@NotNull @RequestParam Long sessionId) {
        return Result.success(messageService.getChatHistory(sessionId));
    }


    /**
     * 获取当前用户的总未读消息数量接口
     *
     * @return 未读数量
     */
    @GetMapping("/chat/unread/count")
    public Result getUnreadCount() {
        return Result.success(messageService.getUnreadCount(SecurityUtils.getCurrentUserId()));
    }


    /**
     * 将会话中的消息标记为已读接口
     *
     * @param sessionId 会话ID
     * @param userId    当前用户ID
     * @return 操作结果
     */
    @GetMapping("/chat/read")
    public Result markRead(
            @RequestParam Long sessionId,
            @RequestParam Long userId) {
        messageService.markSessionAsRead(sessionId, userId);
        return Result.success();
    }
}