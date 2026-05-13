package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 需求类别
 */
@Getter
public enum DemandCategoryEnum {

    TECHNOLOGY(1, "技术需求"),
    RESEARCH(2, "研究需求"),
    INNOVATION(3, "创新创意"),
    OTHER(4, "其他");

    private final Integer code;
    private final String desc;

    DemandCategoryEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DemandCategoryEnum getByCode(Integer code) {
        if (code == null) return null;
        for (DemandCategoryEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }


    /**
     * 根据code获取desc（最常用：直接返回描述）
     * @param code 编码
     * @return 描述，找不到返回null
     */
    public static String getDescByCode(Integer code) {
        DemandCategoryEnum enums = getByCode(code);
        return enums == null ? null : enums.getDesc();
    }
}