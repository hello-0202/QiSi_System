package com.sc.qisi_system.module.websocket.enumType;

import lombok.Getter;

/**
 * WebSocket 用户强制下线原因枚举
 */
@Getter
public enum KickOffReasonEnum {

    // 账号封禁
    BANNED_BY_ADMIN("您的账号已被管理员封禁，无法继续使用"),
    // 账号解封
    UNBANNED("您的账号已解封，请重新登录"),
    // 管理员强制下线
    ADMIN_FORCE_KICK("管理员已强制您下线"),
    // 权限变更（角色/身份修改）
    PERMISSION_CHANGED("您的账号权限已变更，请重新登录"),
    // 用户类型修改
    USER_TYPE_CHANGED("您的用户类型已变更，请重新登录"),
    // 异地登录
    LOGIN_OTHER_DEVICE("您的账号已在另一台设备登录，您已被迫下线"),
    // 登录过期
    TOKEN_EXPIRED("登录状态已过期，请重新登录"),
    // 账号注销
    USER_DELETED("您的账号已注销，感谢使用"),
    // 系统维护
    SYSTEM_MAINTENANCE("系统维护中，暂时断开连接"),
    // 违规操作
    VIOLATION_OPERATION("您因违规操作已被强制下线");

    private final String reason;

    KickOffReasonEnum(String reason) {
        this.reason = reason;
    }
}