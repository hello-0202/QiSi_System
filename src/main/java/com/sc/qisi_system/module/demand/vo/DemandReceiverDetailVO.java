package com.sc.qisi_system.module.demand.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DemandReceiverDetailVO {


    /**
     * 需求ID
     */
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
}