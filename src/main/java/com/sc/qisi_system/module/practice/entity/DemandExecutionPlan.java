package com.sc.qisi_system.module.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.sc.qisi_system.module.apply.domain.ResearchPlanStage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.NoArgsConstructor;


/**
 * 需求执行计划实体类
 * 对应数据库表：demand_execution_plan
 */
@TableName("demand_execution_plan")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemandExecutionPlan implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 关联需求ID: demand主键
     */
    private Long demandId;


    /**
     * 关联申请ID: demand_apply主键
     */
    private Long applyId;


    /**
     * 研究计划
     */
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private List<ResearchPlanStage> researchPlan;


    /**
     * 预计完成时间: 单位：天
     */
    private Integer expectedFinishTime;


    /**
     * 版本号
     */
    @Version
    private Integer version;


    /**
     * 操作人ID
     */
    private Long operatorId;


    /**
     * 修改备注
     */
    private String modifyRemark;


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