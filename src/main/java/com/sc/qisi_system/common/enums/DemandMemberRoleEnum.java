package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 申请人角色
 */
@Getter
public enum DemandMemberRoleEnum {

    NORMAL_MEMBER(1, "普通成员"),
    LEADER(2, "负责人");

    private final Integer code;
    private final String desc;

    DemandMemberRoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DemandMemberRoleEnum getByCode(Integer code) {
        if (code == null) return null;
        for (DemandMemberRoleEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}