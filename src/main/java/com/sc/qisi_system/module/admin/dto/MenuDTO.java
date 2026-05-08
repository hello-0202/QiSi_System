package com.sc.qisi_system.module.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuDTO {


    /**
     * 菜单ID
     */
    private Long id;


    /**
     * 父菜单ID 0为一级菜单
     */
    @NotNull(message = "父菜单不能为空")
    private Long parentId;


    /**
     * 路由地址
     */
    @NotBlank(message = "路由地址不能为空")
    private String path;


    /**
     * 路由名称
     */
    @NotBlank(message = "路由名称不能为空")
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
    @NotBlank(message = "菜单标题不能为空")
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
}
