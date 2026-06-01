package com.sc.qisi_system.module.demand.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sc.qisi_system.module.apply.domain.ResearchPlanStage;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DemandPublicDetailVO {


    /**
     * 需求ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    /**
     * 发布人信息
     */
    private DemandPublisherDetailVO demandPublisherDetailVO;


    /**
     * 需求标题
     */
    private String title;


    /**
     * 需求分类
     */
    private String category;


    /**
     * 研究领域
     */
    private String researchField;


    /**
     * 项目背景
     */
    private String background;


    /**
     * 需求描述
     */
    private String description;


    /**
     * 技术要求
     */
    private String techRequire;


    /**
     * 预期成果
     */
    private String expectedResult;


    /**
     * 研究周期
     */
    private Integer researchCycle;


    /**
     * 最大成员数
     */
    private Integer maxMembers;


    /**
     * 是否需要提交方案
     */
    private Boolean requirePlan;


    /**
     * 申请截止时间
     */
    private LocalDateTime deadline;


    /**
     * 研究计划
     */
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private List<ResearchPlanStage> researchPlan;
}