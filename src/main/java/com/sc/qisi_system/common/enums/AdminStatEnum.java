package com.sc.qisi_system.common.enums;

import lombok.Getter;

@Getter
public enum AdminStatEnum {

    TOTAL_DEMAND("total_demand", "总需求数"),
    PENDING_REVIEW("pending_review", "待审核数量"),
    PUBLISHED("published", "已发布数量"),
    ONGOING("ongoing", "进行中数量"),
    COMPLETED("completed", "已完成数量"),
    TOTAL_USER("total_user", "已注册用户数"),
    TOTAL_APPLY("total_apply", "总申请数量"),
    FINISH_RATE("finish_rate", "项目完成率");

    private final String key;
    private final String label;

    AdminStatEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }
}