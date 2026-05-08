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


@RequestMapping("/api/message")
@RequiredArgsConstructor
@Controller
public class MessageController {


    private final MessageService messageService;


    @MessageMapping("/send/private")
    public void sendPrivateMessage(UserMessageDTO userMessageDTO, SimpMessageHeaderAccessor accessor) {
        messageService.sendPrivateMessage(userMessageDTO, accessor);
    }


    /**
     * 获取的聊天记录
     */
    @GetMapping("/chat/history")
    public Result getChatHistory(
            @NotNull @RequestParam Long targetUserId){
        return Result.success(messageService.getChatHistory(SecurityUtils.getCurrentUserId(), targetUserId));
    }


    @GetMapping("/chat/unread/count")
    public Result getUnreadCount() {
        return Result.success(messageService.getUnreadCount(SecurityUtils.getCurrentUserId()));
    }


    @GetMapping("/chat/read")
    public Result markRead(@RequestParam Long targetUserId) {
        messageService.markAsRead(SecurityUtils.getCurrentUserId(), targetUserId);
        return Result.success();
    }


    @GetMapping("/chat/session/list")
    public Result getChatSessionList() {
        return Result.success(messageService.getChatSessionList(SecurityUtils.getCurrentUserId()));
    }
}
