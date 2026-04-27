package com.sc.qisi_system.module.practice.vo;

import lombok.Data;

@Data
public class DemandProgressVO {


    /**
     * 关联需求ID: demand主键
     */
    private Long demandId;


    /**
     * 提交人ID: sys_user主键
     */
    private Long userId;


    /**
     * 提交人姓名
     */
    private String name;


    /**
     * 关联团队ID: team主键
     */
    private Long teamId;


    /**
     * 已完成工作
     */
    private String workDone;


    /**
     * 遇到的问题
     */
    private String problem;


    /**
     * 下一步计划
     */
    private String nextPlan;
}
