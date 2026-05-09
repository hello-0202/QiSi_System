package com.sc.qisi_system.module.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sc.qisi_system.common.enums.SessionTypeEnum;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.service.SysUserService;
import com.sc.qisi_system.module.websocket.dto.UserMessageDTO;
import com.sc.qisi_system.module.websocket.entity.ChatMessage;
import com.sc.qisi_system.module.websocket.entity.ChatSession;
import com.sc.qisi_system.module.websocket.mapper.ChatMessageMapper;
import com.sc.qisi_system.module.websocket.mapper.ChatSessionMapper;
import com.sc.qisi_system.module.websocket.service.MessageService;
import com.sc.qisi_system.module.websocket.vo.ChatSessionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final ChatMessageMapper chatMessageMapper;
    private final ChatSessionMapper chatSessionMapper;
    private final RedisService redisService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MinioService minioService;
    private final SysUserService sysUserService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendPrivateMessage(UserMessageDTO userMessageDTO, SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        Long fromUserId = redisService.getUserId(sessionId);
        Long toUserId = userMessageDTO.getToUserId();

        // 1. 获取或创建对话
        ChatSession session = getOrCreateSession(fromUserId, toUserId);

        // 2. 构建消息
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(userMessageDTO, chatMessage);
        chatMessage.setSessionId(session.getId());
        chatMessage.setFromUserId(fromUserId);
        chatMessage.setStatus(0);

        // 3. 插入消息
        chatMessageMapper.insert(chatMessage);

        // 4. 更新会话最后一条消息
        session.setLastMessage(chatMessage.getContent());
        session.setLastTime(LocalDateTime.now());
        chatSessionMapper.updateById(session);

        // 5. 推送消息
        String destination = "/queue/private";
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(toUserId), destination, chatMessage);
    }


    @Override
    public ChatSession getOrCreateSession(Long currentUserId, Long targetUserId) {
        ChatSession chatSession = getChatSession(currentUserId, targetUserId);
        if (chatSession == null) {
            chatSession = new ChatSession();
            chatSession.setUserA(currentUserId);
            chatSession.setUserB(targetUserId);
            chatSession.setSessionType(SessionTypeEnum.SINGLE.getCode());
            chatSessionMapper.insert(chatSession);
        }
        return chatSession;
    }


    @Override
    public List<ChatMessage> getChatHistory(Long sessionId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreateTime);
        return chatMessageMapper.selectList(wrapper);
    }


    @Override
    public Long getUnreadCount(Long userId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getStatus, 0);
        return chatMessageMapper.selectCount(wrapper);
    }


    @Transactional
    @Override
    public void markSessionAsRead(Long sessionId, Long userId) {
        LambdaUpdateWrapper<ChatMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper
                .eq(ChatMessage::getSessionId, sessionId)
                .eq(ChatMessage::getStatus, 0)
                .set(ChatMessage::getStatus, 1);
        chatMessageMapper.update(null, wrapper);
    }


    @Override
    public List<ChatSessionVO> getChatSessionList(Long currentUserId) {
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatSession::getSessionType, SessionTypeEnum.SINGLE.getCode())
                .and(w -> w.eq(ChatSession::getUserA, currentUserId)
                        .or()
                        .eq(ChatSession::getUserB, currentUserId))
                .orderByDesc(ChatSession::getLastTime);

        List<ChatSession> sessions = chatSessionMapper.selectList(wrapper);
        Map<Long, ChatSessionVO> sessionMap = new HashMap<>();

        for (ChatSession session : sessions) {
            Long targetId = session.getUserA().equals(currentUserId) ? session.getUserB() : session.getUserA();

            if (sessionMap.containsKey(targetId)) continue;

            SysUser sysUser = sysUserService.getOne(
                    new LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getId, targetId)
                            .select(SysUser::getAvatar, SysUser::getName)
            );

            if (sysUser == null) continue;

            ChatSessionVO vo = new ChatSessionVO();
            vo.setUserId(targetId);
            vo.setSessionId(session.getId());
            vo.setLastMessage(session.getLastMessage());
            vo.setLastTime(session.getLastTime());
            vo.setAvatar(minioService.getUserAvatarUrl(sysUser.getAvatar()));
            vo.setNickname(sysUser.getName());
            vo.setUnreadCount(getUnreadCountBySession(session.getId()));

            sessionMap.put(targetId, vo);
        }
        return new ArrayList<>(sessionMap.values());
    }


    private int getUnreadCountBySession(Long sessionId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
                .eq(ChatMessage::getStatus, 0);
        return chatMessageMapper.selectCount(wrapper).intValue();
    }


    private ChatSession getChatSession(Long currentUserId, Long targetUserId) {
        LambdaQueryWrapper<ChatSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .and(w -> w.eq(ChatSession::getUserA, currentUserId).eq(ChatSession::getUserB, targetUserId))
                .or(w -> w.eq(ChatSession::getUserA, targetUserId).eq(ChatSession::getUserB, currentUserId));
        return chatSessionMapper.selectOne(queryWrapper);
    }
}