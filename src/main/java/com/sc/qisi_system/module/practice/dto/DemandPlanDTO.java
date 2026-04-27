package com.sc.qisi_system.module.practice.dto;


import com.sc.qisi_system.module.apply.domain.ResearchPlanStage;
import lombok.Data;

import java.util.List;

@Data
public class DemandPlanDTO {


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
    private List<ResearchPlanStage> researchPlan;


    /**
     * 预计完成时间: 单位：天
     */
    private Integer expectedFinishTime;


    /**
     * 修改备注
     */
    private String modifyRemark;
}
