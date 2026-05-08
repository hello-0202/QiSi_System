package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 用户类型枚举
 * 对应权限系统：认领者 / 发布者 / 管理员
 */
@Getter
public enum UserIdentityEnum {

    /**
     * 认领者（学生）
     */
    CLAIMANT(1, "认领者"),

    /**
     * 发布者（教师/企业）
     */
    PUBLISHER(2, "发布者"),

    /**
     * 系统管理员
     */
    ADMIN(3, "管理员");

    private final Integer code;
    private final String desc;

    UserIdentityEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 获取枚举
     */
    public static UserIdentityEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserIdentityEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}