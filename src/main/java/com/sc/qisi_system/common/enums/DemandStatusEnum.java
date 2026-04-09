package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求全流程状态
 */
@Getter
public enum DemandStatusEnum {

    DRAFT(0, "草稿"),
    REVIEWING(1, "审核中"),
    REJECTED(2, "已驳回"),
    PUBLISHED(3, "已发布"),
    RESEARCHING(4, "研究中"),
    COMPLETED(5, "已完成"),
    CLOSED(6, "已关闭");

    private final Integer code;
    private final String desc;

    DemandStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DemandStatusEnum getByCode(Integer code) {
        if (code == null) return null;
        for (DemandStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}