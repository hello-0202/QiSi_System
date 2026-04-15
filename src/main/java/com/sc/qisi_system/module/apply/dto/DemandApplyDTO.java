package com.sc.qisi_system.module.apply.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;


/**
 * 研究申请 DTO
 * 用于接收前端传递的研究相关参数
 */
@Data
public class DemandApplyDTO {


    /**
     * 关联需求ID
     */
    private Long demandId;


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
