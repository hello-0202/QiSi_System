package com.sc.qisi_system.module.apply.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplyDetailVO {


    /**
     * 研究思路
     */
    private String researchIdea;


    /**
     * 研究计划
     */
    private JSONObject researchPlan;


    /**
     * 预计完成时间: 单位：月
     */
    private Integer expectedFinishTime;


    /**
     * 相关经验
     */
    private String relevantExperience;


    /**
     * 审核状态: 0-待审核 1-已通过 2-已拒绝
     */
    private Integer auditStatus;


    /**
     * 审核人ID: sys_user表主键
     */
    private Long auditUserId;


    /**
     * 审核备注
     */
    private String auditRemark;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
