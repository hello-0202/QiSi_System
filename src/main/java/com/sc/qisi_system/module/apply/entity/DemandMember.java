package com.sc.qisi_system.module.apply.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 需求成员实体类
 * 对应数据库表：demand_member
 */
@TableName("demand_member")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemandMember implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键id: 雪花算法
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 需求id: 关联demand表
     */
    private Long demandId;


    /**
     * 成员id: 关联sys_user表
     */
    private Long userId;


    /**
     * 状态：1-已通过 2-已退出 3-已移除 4-已归档
     */
    private Integer status;


    /**
     * 成员角色: 1-普通成员 2-负责人
     */
    private Integer roleType;


    /**
     * 加入时间
     */
    private LocalDateTime joinTime;


    /**
     * 退出/移除时间
     */
    private LocalDateTime quitTime;


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