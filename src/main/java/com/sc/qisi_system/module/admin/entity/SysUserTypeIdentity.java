package com.sc.qisi_system.module.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 用户类型 - 业务身份映射表
 */
@Data
@TableName("sys_user_type_identity")
public class SysUserTypeIdentity {


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 用户类型：1-学生 2-教师 3-企业人员 4-管理员
     */
    private Integer userType;


    /**
     * 业务身份ID：1-认领者 2-发布者 3-管理员
     */
    private Integer identityId;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}