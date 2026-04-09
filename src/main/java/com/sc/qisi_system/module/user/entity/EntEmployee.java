package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业员工实体类
 * 对应数据库表：ent_employee
 */
@TableName("ent_employee")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntEmployee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联用户ID: sys_user表主键
     */
    private Long userId;

    /**
     * 关联企业ID
     */
    private Long enterpriseId;

    /**
     * 员工编号
     */
    private String employeeNo;

    /**
     * 职位名称
     */
    private String jobTitle;

    /**
     * 部门
     */
    private String department;

    /**
     * 是否为企业联系人: 0-否 1-是
     */
    private Integer isContact;

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