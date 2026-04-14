package com.sc.qisi_system.module.demand.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DemandPublishDraftDTO {


    /**
     * 发布人ID: sys_user主键
     */
    @NotNull(message = "发布人id不能为空")
    private Long publisherId;


    /**
     * 发布类型: 1-个人发布 2-团队发布
     */
    private Integer publishType;


    /**
     * 发布团队ID: 个人发布时可为空
     */
    private Long publishTeamId;


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
     * 研究开始时间
     */
    private LocalDateTime startTime;


    /**
     * 研究结束时间
     */
    private LocalDateTime endTime;


    /**
     * 研究周期: 单位：天
     */
    private Integer researchCycle;


    /**
     * 最大成员数
     */
    private Integer maxMembers;


    /**
     * 是否需要提交方案: 0-不需要 1-需要
     */
    private Boolean requirePlan;


    /**
     * 申请截止时间
     */
    private LocalDateTime deadline;


    /**
     * 进度百分比: 0-100
     */
    private Integer progressPercent;
}
