package com.sc.qisi_system.module.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 角色(业务身份)菜单关联表
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 业务身份
     */
    private Long identityId;


    /**
     * 菜单ID
     * 对应 sys_menu.id
     */
    private Long menuId;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}