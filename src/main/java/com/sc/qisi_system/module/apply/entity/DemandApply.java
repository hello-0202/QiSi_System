package com.sc.qisi_system.module.apply.entity;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.sc.qisi_system.module.apply.domain.ResearchPlanStage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 需求申请实体类
 * 对应数据库表：demand_apply
 */
@TableName(value = "demand_apply", autoResultMap = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemandApply implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 关联需求ID
     */
    private Long demandId;


    /**
     * 申请用户ID
     */
    private Long userId;


    /**
     * 研究思路
     */
    private String researchIdea;


    /**
     * 研究计划
     */
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private List<ResearchPlanStage> researchPlan;


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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}