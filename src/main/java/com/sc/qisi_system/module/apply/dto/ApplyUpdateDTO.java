package com.sc.qisi_system.module.apply.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.sc.qisi_system.module.apply.domain.ResearchPlanStage;
import lombok.Data;

import java.util.List;

@Data
public class ApplyUpdateDTO {


    /**
     * 关联需求id
     */
    private Long demandId;


    /**
     * 申请表id
     */
    private Long applyId;


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
}
