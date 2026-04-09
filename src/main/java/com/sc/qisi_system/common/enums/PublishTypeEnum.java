package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求发布类型
 */
@Getter
public enum PublishTypeEnum {

    PERSON(1, "个人发布"),
    TEAM(2, "团队发布");

    private final Integer code;
    private final String desc;

    PublishTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PublishTypeEnum getByCode(Integer code) {
        if (code == null) return null;
        for (PublishTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}