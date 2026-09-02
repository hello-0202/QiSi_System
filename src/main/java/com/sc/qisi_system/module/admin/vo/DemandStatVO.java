package com.sc.qisi_system.module.admin.vo;

import lombok.Data;

@Data
public class DemandStatVO {

    /**
     * 待审核数量
     */
    private Long waitAuditCount;

    /**
     * 今日新增数量
     */
    private Long todayNewCount;

    /**
     * 已通过数量
     */
    private Long passCount;

    /**
     * 已拒绝数量
     */
    private Long rejectCount;
}
