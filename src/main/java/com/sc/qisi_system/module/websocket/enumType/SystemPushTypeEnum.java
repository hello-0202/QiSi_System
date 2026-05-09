package com.sc.qisi_system.module.websocket.enumType;

import lombok.Getter;

@Getter
public enum SystemPushTypeEnum {

    /**
     * 权限映射配置已更新（你这个场景）
     */
    IDENTITY_MAP_UPDATED("权限配置已更新，请刷新页面"),

    /**
     * 全局系统公告
     */
    SYSTEM_NOTICE("系统公告"),

    /**
     * 全局配置更新
     */
    CONFIG_CHANGED("系统配置已变更"),

    ;

    private final String msg;

    SystemPushTypeEnum(String msg) {
        this.msg = msg;
    }
}