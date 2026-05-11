package com.sc.qisi_system.module.user.service.impl;

import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.service.UserOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


/**
 * 用户在线状态服务实现类
 */
@RequiredArgsConstructor
@Service
public class UserOnlineServiceImpl implements UserOnlineService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisService redisService;


    /**
     * 踢出旧会话（顶号登录）
     */
    @Override
    public void kickOutOldSession(Long userId, String oldSessionId) {
        // 1. 向旧会话发送被踢下线通知
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/kick",
                ResultCode.ACCOUNT_LOGIN_OTHER_DEVICE.getMessage()
        );

        // 2. 删除旧的 Redis 会话映射
        redisService.removeMapping(userId, oldSessionId);
    }
}