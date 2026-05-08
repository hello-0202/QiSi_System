package com.sc.qisi_system.module.admin.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("sys_menu")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SysMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 菜单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 父菜单ID 0为一级菜单
     */
    private Long parentId;


    /**
     * 路由地址
     */
    private String path;


    /**
     * 路由名称
     */
    private String name;


    /**
     * 组件路径
     */
    private String component;


    /**
     * 父菜单重定向地址
     */
    private String redirect;


    /**
     * 菜单显示标题
     */
    private String title;


    /**
     * 菜单图标
     */
    private String icon;


    /**
     * 菜单排序
     */
    private Integer sort;


    /**
     * 0显示侧边菜单 1隐藏仅路由
     */
    private Integer hidden;


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
