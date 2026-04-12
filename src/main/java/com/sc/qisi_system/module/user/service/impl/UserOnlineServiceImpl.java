package com.sc.qisi_system.module.user.service.impl;

import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.service.UserOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserOnlineServiceImpl implements UserOnlineService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisService redisService;

    @Override
    public void kickOutOldSession(Long userId, String oldSessionId) {

        // 1.发送业务通知
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/kick",
                ResultCode.ACCOUNT_LOGIN_OTHER_DEVICE.getMessage()
        );

        // 2.删除旧的 Redis 映射
        redisService.removeMapping(userId, oldSessionId);
    }
}
