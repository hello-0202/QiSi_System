package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 用户角色
 */
@Getter
public enum UserTypeEnum {

    STUDENT(1, "ROLE_STUDENT", "学生"),
    TEACHER(2, "ROLE_TEACHER", "教师"),
    COMPANY(3, "ROLE_COMPANY", "企业人员"),
    ADMIN(4, "ROLE_ADMIN", "管理员");

    private final Integer code;
    private final String authority;
    private final String desc;

    UserTypeEnum(Integer code, String authority, String desc) {
        this.code = code;
        this.authority = authority;
        this.desc = desc;
    }

    public static UserTypeEnum getByCode(Integer code) {
        if (code == null) return null;
        for (UserTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescDescByCode(Integer code) {
        if (code == null) return null;
        for (UserTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        if (code == null) return null;
        for (UserTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.name();
            }
        }
        return null;
    }

}
