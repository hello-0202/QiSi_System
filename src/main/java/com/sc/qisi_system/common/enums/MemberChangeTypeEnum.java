package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求成员更改类型
 */
@Getter
public enum MemberChangeTypeEnum {

    QUIT_APPLY(1, "主动退出申请"),
    PUBLISHER_REMOVE(2, "发布者移除");

    private final Integer code;
    private final String desc;

    MemberChangeTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MemberChangeTypeEnum getByCode(Integer code) {
        if (code == null) return null;
        for (MemberChangeTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}