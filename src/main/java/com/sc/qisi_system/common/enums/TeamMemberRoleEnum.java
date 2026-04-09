package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 团队成员角色
 */
@Getter
public enum TeamMemberRoleEnum {
    LEADER(1, "负责人"),
    MEMBER(2, "普通成员");

    private final Integer code;
    private final String desc;

    TeamMemberRoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TeamMemberRoleEnum getByCode(Integer code) {
        if (code == null) return null;
        for (TeamMemberRoleEnum e : values()) {
            if (e.getCode().equals(code)) return e;
        }
        return null;
    }
}