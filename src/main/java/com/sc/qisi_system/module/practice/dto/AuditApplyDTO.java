package com.sc.qisi_system.module.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AuditApplyDTO {


    @NotNull(message = "申请ID不能为空")
    private Long applyId;


    /**
     * 需求id: 关联demand表
     */
    private Long demandId;


    @NotNull(message = "申请人id不能为空")
    private Long applicantId;


    @NotNull(message = "审核状态不能为空：1通过 2拒绝")
    private Integer auditStatus;


    private String auditRemark;


    /**
     * 成员角色: 1-普通成员 2-负责人
     */
    private Integer roleType;
}
