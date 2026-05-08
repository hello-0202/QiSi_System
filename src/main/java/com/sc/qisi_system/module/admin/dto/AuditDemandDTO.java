package com.sc.qisi_system.module.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditDemandDTO {


    /**
     * 需求id: 关联demand表
     */
    @NotNull
    private Long demandId;


    /**
     * 需求状态: 0-草稿 1审核中 2-已驳回 3-已发布 4-研究中 5-已完成 6-已关闭
     */
    @NotNull
    private Integer status;


    /**
     * 审核意见
     */
    private String auditRemark;
}
