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
 * 功能: 私人消息发送、会话管理、聊天记录查询、未读消息统计、消息已读标记等聊天业务逻辑
 */
public interface MessageService {


    /**
     * 发送私人私信
     * 角色: 认领者 发布者 管理员
     *
     * @param userMessageDTO 消息内容参数
     * @param accessor WebSocket 上下文信息
     */
    void sendPrivateMessage(UserMessageDTO userMessageDTO, SimpMessageHeaderAccessor accessor);


    /**
     * 获取或创建单聊会话
     * 角色: 认领者 发布者 管理员
     *
     * @param currentUserId 当前用户ID
     * @param targetUserId  对方用户ID
     * @return 会话信息实体
     */
    ChatSession getOrCreateSession(Long currentUserId, Long targetUserId);


    /**
     * 获取会话的聊天记录
     * 角色: 认领者 发布者 管理员
     *
     * @param sessionId 会话ID
     * @return 聊天消息列表
     */
    List<ChatMessage> getChatHistory(Long sessionId);


    /**
     * 获取指定会话未读消息数量
     * 角色: 认领者 发布者 管理员
     *
     * @param sessionId 会话ID
     * @return 未读消息总数
     */
    Long getUnreadCount(Long sessionId);


    /**
     * 将会话消息标记为已读
     * 角色: 认领者 发布者 管理员
     *
     * @param sessionId 会话ID
     * @param userId    当前用户ID
     */
    @Transactional
    void markSessionAsRead(Long sessionId, Long userId);


    /**
     * 获取当前用户的会话列表
     * 角色: 认领者 发布者 管理员
     *
     * @param currentUserId 当前用户ID
     * @return 会话展示列表VO
     */
    List<ChatSessionVO> getChatSessionList(Long currentUserId);
}