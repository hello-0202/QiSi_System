package com.sc.qisi_system.module.websocket.service;

import com.sc.qisi_system.module.websocket.dto.UserMessageDTO;
import com.sc.qisi_system.module.websocket.entity.ChatMessage;
import com.sc.qisi_system.module.websocket.entity.ChatSession;
import com.sc.qisi_system.module.websocket.vo.ChatSessionVO;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 聊天消息服务接口
 */
public interface MessageService {


    /**
     * 发送私人私信
     *
     * @param userMessageDTO 消息内容
     * @param accessor websocket 上下文
     */
    void sendPrivateMessage(UserMessageDTO userMessageDTO, SimpMessageHeaderAccessor accessor);


    /**
     * 获取或创建单聊会话
     *
     * @param currentUserId 当前用户ID
     * @param targetUserId  对方用户ID
     * @return 会话信息
     */
    ChatSession getOrCreateSession(Long currentUserId, Long targetUserId);


    /**
     * 获取会话的聊天记录
     *
     * @param sessionId 会话ID
     * @return 消息列表
     */
    List<ChatMessage> getChatHistory(Long sessionId);


    /**
     * 获取用户总未读消息数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    Long getUnreadCount(Long userId);


    /**
     * 将会话消息标记为已读
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     */
    @Transactional
    void markSessionAsRead(Long sessionId, Long userId);


    /**
     * 获取当前用户的会话列表
     *
     * @param currentUserId 当前用户ID
     * @return 会话列表VO
     */
    List<ChatSessionVO> getChatSessionList(Long currentUserId);
}