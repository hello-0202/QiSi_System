package com.sc.qisi_system.module.user.service;

public interface UserOnlineService {
    void kickOutOldSession(Long userId, String oldSessionId);
}
