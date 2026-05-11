package com.sc.qisi_system.module.websocket.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.websocket.dto.UserMessageDTO;
import com.sc.qisi_system.module.websocket.service.MessageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 聊天消息管理控制器
 * 功能: 私信发送、单聊会话创建与获取、会话列表、聊天记录查询、未读消息统计、消息已读标记
 */
@RequestMapping("/api/message")
@RequiredArgsConstructor
@RestController
public class MessageController {


    private final MessageService messageService;

    /**
     * WebSocket发送私人私信
     * 角色: 认领者 发布者 管理员
     *
     * @param userMessageDTO 私信消息参数
     * @param accessor 消息头访问器
     */
    @MessageMapping("/send/private")
    public void sendPrivateMessage(UserMessageDTO userMessageDTO, SimpMessageHeaderAccessor accessor) {
        messageService.sendPrivateMessage(userMessageDTO, accessor);
    }


    /**
     * 创建或获取单聊会话接口
     * 角色: 认领者 发布者 管理员
     *
     * @param targetUserId 对方用户ID
     * @return 会话信息及会话ID
     */
    @GetMapping("/create-or-get")
    public Result createOrGetSession(@RequestParam Long targetUserId) {
        return Result.success(messageService.getOrCreateSession(SecurityUtils.getCurrentUserId(), targetUserId));
    }


    /**
     * 获取当前用户聊天会话列表接口
     * 角色: 认领者 发布者 管理员
     *
     * @return 聊天会话列表数据
     */
    @GetMapping("/chat/session/list")
    public Result getChatSessionList() {
        return Result.success(messageService.getChatSessionList(SecurityUtils.getCurrentUserId()));
    }


    /**
     * 获取指定会话聊天记录接口
     * 角色: 认领者 发布者 管理员
     *
     * @param sessionId 会话ID
     * @return 聊天记录列表
     */
    @GetMapping("/chat/history")
    public Result getChatHistory(@NotNull @RequestParam Long sessionId) {
        return Result.success(messageService.getChatHistory(sessionId));
    }


    /**
     * 获取指定会话未读消息数量接口
     * 角色: 认领者 发布者 管理员
     *
     * @param sessionId 会话ID
     * @return 会话未读消息数量
     */
    @GetMapping("/chat/unread/count")
    public Result getUnreadCount(
            @RequestParam Long sessionId) {
        return Result.success(messageService.getUnreadCount(sessionId));
    }


    /**
     * 将会话消息标记为已读接口
     * 角色: 认领者 发布者 管理员
     *
     * @param sessionId 会话ID
     * @param userId 当前用户ID
     * @return 统一返回结果
     */
    @GetMapping("/chat/read")
    public Result markRead(
            @RequestParam Long sessionId,
            @RequestParam Long userId) {
        messageService.markSessionAsRead(sessionId, userId);
        return Result.success();
    }
}