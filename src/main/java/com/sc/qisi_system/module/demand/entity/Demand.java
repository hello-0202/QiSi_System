package com.sc.qisi_system.module.demand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("demand")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Demand implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 发布人ID: sys_user主键
     */
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
    private Integer category;


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
     * 需求状态: 0-草稿 1审核中 2-已驳回 3-已发布 4-研究中 5-已完成 6-已关闭
     */
    private Integer status;


    /**
     * 申请截止时间
     */
    private LocalDateTime deadline;


    /**
     * 是否关闭申请: 0-无申请 1-待审核 2-已拒绝
     */
    private Integer closeApplyStatus;


    /**
     * 进度百分比: 0-100
     */
    private Integer progressPercent;


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
