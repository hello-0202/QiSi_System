package com.sc.qisi_system.module.apply.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

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
    private JSONObject researchPlan;


    /**
     * 预计完成时间: 单位：月
     */
    private Integer expectedFinishTime;


    /**
     * 相关经验
     */
    private String relevantExperience;
}
