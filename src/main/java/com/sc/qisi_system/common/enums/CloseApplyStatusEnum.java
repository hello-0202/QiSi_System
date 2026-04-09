package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求关闭状态
 */
@Getter
public enum CloseApplyStatusEnum {

    NO_APPLY(0, "无申请"),
    PENDING(1, "待审核"),
    REFUSED(2, "已拒绝");

    private final Integer code;
    private final String desc;

    CloseApplyStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CloseApplyStatusEnum getByCode(Integer code) {
        if (code == null) return null;
        for (CloseApplyStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}