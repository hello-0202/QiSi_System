package com.sc.qisi_system.common.enums;

import lombok.Getter;

/**
 * 工作台统计类型枚举
 */
@Getter
public enum AdminWorkbenchStatEnum {

    TOTAL_DEMANDS("total_demands", "需求总数"),
    ONGOING_PROJECTS("ongoing_projects", "进行中项目"),
    COMPLETED_PROJECTS("completed_projects", "已完成项目"),
    PENDING_REVIEW("pending_review", "待审核");

    private final String key;
    private final String label;

    AdminWorkbenchStatEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }

}