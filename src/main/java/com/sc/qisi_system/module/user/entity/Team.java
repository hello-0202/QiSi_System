package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 团队实体类
 * 对应数据库表：team
 */
@TableName("team")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Team implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 团队名称
     */
    private String teamName;

    /**
     * 团队简介
     */
    private String teamIntro;

    /**
     * 团队负责人ID: sys_user主键
     */
    private Long leaderId;

    /**
     * 团队类型：1-科研团队 2-实验室团队 3-学生团队 4-企业团队 5-校外合作团队
     */
    private Integer teamType;

    /**
     * 状态：0-禁用 1-正常
     */
    private boolean status;

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