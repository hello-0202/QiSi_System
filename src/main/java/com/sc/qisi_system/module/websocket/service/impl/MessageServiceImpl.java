package com.sc.qisi_system.module.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.service.SysUserService;
import com.sc.qisi_system.module.websocket.dto.UserMessageDTO;
import com.sc.qisi_system.module.websocket.entity.UserMessage;
import com.sc.qisi_system.module.websocket.mapper.UserMessageMapper;
import com.sc.qisi_system.module.websocket.service.MessageService;
import com.sc.qisi_system.module.websocket.vo.ChatSessionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {


    private final UserMessageMapper userMessageMapper;
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

        UserMessage userMessage = new UserMessage();
        BeanUtils.copyProperties(userMessageDTO, userMessage);
        userMessage.setFromUserId(fromUserId);
        userMessage.setStatus(0);
        userMessageMapper.insert(userMessage);

        String destination = "/queue/private";
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(toUserId),destination,userMessage);
    }


    @Override
    public List<UserMessage> getChatHistory(Long currentUserId, Long targetUserId) {
        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(UserMessage::getFromUserId, currentUserId)
                        .eq(UserMessage::getToUserId, targetUserId))
                .or(w -> w.eq(UserMessage::getFromUserId, targetUserId)
                        .eq(UserMessage::getToUserId, currentUserId));

        wrapper.orderByAsc(UserMessage::getCreateTime);
        return userMessageMapper.selectList(wrapper);
    }


    @Override
    public Long getUnreadCount(Long userId) {
        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMessage::getToUserId, userId)
                .eq(UserMessage::getStatus, 0);
        return userMessageMapper.selectCount(wrapper);
    }


    @Transactional
    @Override
    public void markAsRead(Long myUserId, Long targetUserId) {
        LambdaUpdateWrapper<UserMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserMessage::getToUserId, myUserId)
                .eq(UserMessage::getFromUserId, targetUserId)
                .eq(UserMessage::getStatus, 0)
                .set(UserMessage::getStatus, 1);
        userMessageMapper.update(null, wrapper);
    }


    @Override
    public List<ChatSessionVO> getChatSessionList(Long currentUserId) {

        // 1. 查询所有和我相关的消息
        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMessage::getToUserId, currentUserId)
                .or()
                .eq(UserMessage::getFromUserId, currentUserId);
        wrapper.orderByDesc(UserMessage::getCreateTime);

        List<UserMessage> messages = userMessageMapper.selectList(wrapper);

        // 2. 分组 → 每个聊天对象只保留一条
        Map<Long, ChatSessionVO> sessionMap = new HashMap<>();

        for (UserMessage msg : messages) {
            Long fromUserId = msg.getFromUserId();
            Long toUserId = msg.getToUserId();

            // 对方ID
            Long targetId = fromUserId.equals(currentUserId) ? toUserId : fromUserId;

            // 已经存在就跳过，只保留最新一条
            if (sessionMap.containsKey(targetId)) {
                continue;
            }

            LambdaQueryWrapper<SysUser> userQuery = new LambdaQueryWrapper<>();
            userQuery.eq(SysUser::getId, targetId)
                    .select(
                            SysUser::getAvatar,
                            SysUser::getName
                    );
            SysUser sysUser = sysUserService.getOne(userQuery);

            if (sysUser == null) {
                continue;
            }

            // 构建VO
            ChatSessionVO vo = new ChatSessionVO();
            vo.setUserId(targetId);
            vo.setLastMessage(msg.getContent());
            vo.setLastTime(msg.getCreateTime());
            vo.setAvatar(minioService.getUserAvatarUrl(sysUser.getAvatar()));
            vo.setNickname(sysUser.getName());

            // 未读数量
            int unread = getUnreadCountByTarget(currentUserId, targetId);
            vo.setUnreadCount(unread);

            sessionMap.put(targetId, vo);
        }

        return new ArrayList<>(sessionMap.values());
    }

    // 未读数量
    private int getUnreadCountByTarget(Long myUserId, Long targetId) {
        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMessage::getToUserId, myUserId)
                .eq(UserMessage::getFromUserId, targetId)
                .eq(UserMessage::getStatus, 0);
        return userMessageMapper.selectCount(wrapper).intValue();
    }
}
