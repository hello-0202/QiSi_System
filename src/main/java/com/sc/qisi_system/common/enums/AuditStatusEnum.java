package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求申请状态
 */
@Getter
public enum AuditStatusEnum {
    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已拒绝");

    private final Integer code;
    private final String desc;

    AuditStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuditStatusEnum getByCode(Integer code) {
        if (code == null) return null;
        for (AuditStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}