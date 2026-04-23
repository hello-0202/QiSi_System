package com.sc.qisi_system.module.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 需求进度实体类
 * 对应数据库表：demand_progress
 */
@TableName("demand_progress")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemandProgress implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 关联需求ID: demand主键
     */
    private Long demandId;


    /**
     * 提交人ID: sys_user主键
     */
    private Long userId;


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