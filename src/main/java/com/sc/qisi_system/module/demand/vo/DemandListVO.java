package com.sc.qisi_system.module.demand.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DemandListVO {


    /**
     * 主键
     */
    private Long id;


    /**
     * 发布人信息
     */
    private DemandPublisherListVO demandPublisherListVO;


    /**
     * 该需求申请人相关信息
     */
    private DemandApplyListVO demandApplyListVO;


    /**
     * 需求标题
     */
    private String title;


    /**
     * 需求分类
     */
    private Integer category;


    /**
     * 研究周期: 单位：天
     */
    private Integer researchCycle;


    /**
     * 是否需要提交方案: 0-不需要 1-需要
     */
    private Boolean requirePlan;


    /**
     * 需求状态: 0-草稿 1审核中 2-已驳回 3-已发布 4-研究中 5-已完成 6-已关闭
     */
    private Integer status;


    /**
     * 申请截止时间
     */
    private LocalDateTime deadline;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
