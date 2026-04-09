package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求成员更改状态
 */
@Getter
public enum MemberChangeStatusEnum {

    PENDING(0, "待审核"),
    EFFECTIVE(1, "已生效"),
    REFUSED(2, "已拒绝");

    private final Integer code;
    private final String desc;

    MemberChangeStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MemberChangeStatusEnum getByCode(Integer code) {
        if (code == null) return null;
        for (MemberChangeStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}