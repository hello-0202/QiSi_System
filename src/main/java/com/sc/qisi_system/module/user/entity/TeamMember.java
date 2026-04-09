package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 团队成员实体类
 * 对应数据库表：team_member
 */
@TableName("team_member")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 团队ID: team主键
     */
    private Long teamId;

    /**
     * 用户ID: sys_user主键
     */
    private Long userId;

    /**
     * 角色类型: 1-负责人 2-普通成员
     */
    private Integer roleType;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

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