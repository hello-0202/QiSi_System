package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求成员状态
 */
@Getter
public enum MemberStatusEnum {

    PASSED(1, "已通过"),
    QUIT(2, "已退出"),
    REMOVED(3, "已移除"),
    ARCHIVED(4, "已归档");

    private final Integer code;
    private final String desc;

    MemberStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MemberStatusEnum getByCode(Integer code) {
        if (code == null) return null;
        for (MemberStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
