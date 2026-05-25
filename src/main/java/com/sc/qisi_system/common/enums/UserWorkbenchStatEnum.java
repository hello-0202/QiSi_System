package com.sc.qisi_system.common.enums;

import lombok.Getter;

@Getter
public enum UserWorkbenchStatEnum {

    MY_APPLY("my_apply", "我的申请数量"),
    ONGOING("ongoing", "研究中的数量"),
    COMPLETED("completed", "已完成的数量"),
    PENDING_NOTICE("pending_notice", "待处理通知");

    private final String key;
    private final String label;

    UserWorkbenchStatEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }

}