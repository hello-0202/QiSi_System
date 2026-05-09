package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 聊天会话类型枚举
 * 1-单聊  2-群聊
 */
@Getter
public enum SessionTypeEnum {

    SINGLE(1, "单聊"),
    GROUP(2, "群聊");

    private final Integer code;
    private final String desc;

    SessionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static SessionTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SessionTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}