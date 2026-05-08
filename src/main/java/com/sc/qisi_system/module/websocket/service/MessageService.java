package com.sc.qisi_system.module.websocket.service;


import com.sc.qisi_system.module.websocket.dto.UserMessageDTO;
import com.sc.qisi_system.module.websocket.entity.UserMessage;
import com.sc.qisi_system.module.websocket.vo.ChatSessionVO;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageService{

    void sendPrivateMessage(UserMessageDTO userMessageDTO, SimpMessageHeaderAccessor accessor);


    List<UserMessage> getChatHistory(Long toUserId, Long fromUserId);

    Long getUnreadCount(Long userId);

    @Transactional
    void markAsRead(Long myUserId, Long targetUserId);

    List<ChatSessionVO> getChatSessionList(Long currentUserId);
}
