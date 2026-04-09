package com.qisisystem.module.apply.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 需求成员变更记录实体类
 * 对应数据库表：demand_member_change
 */
@TableName("demand_member_change")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemandMemberChange implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联需求ID
     */
    private Long demandId;

    /**
     * 变更用户ID
     */
    private Long userId;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 变更类型: 1-主动退出申请 2-发布者移除
     */
    private Integer changeType;

    /**
     * 审核状态: 0-待审核 1-已生效 2-已拒绝
     */
    private Integer status;

    /**
     * 变更原因
     */
    private String reason;

    /**
     * 审核人ID
     */
    private Long auditUserId;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

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