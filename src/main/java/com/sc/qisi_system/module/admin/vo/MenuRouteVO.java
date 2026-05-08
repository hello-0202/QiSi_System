package com.sc.qisi_system.module.admin.vo;

import lombok.Data;
import java.util.List;

/**
 * 前端路由 VO（返回给前端的菜单结构）
 */
@Data
public class MenuRouteVO {


    /**
     * 菜单ID
     */
    private Long id;


    /**
     * 路由地址
     */
    private String path;


    /**
     * 路由名称
     */
    private String name;


    /**
     * 页面组件路径
     */
    private String component;


    /**
     * 重定向（只有一级菜单需要）
     */
    private String redirect;


    /**
     * 菜单元信息（标题、图标、是否隐藏）
     */
    private Meta meta;


    /**
     * 子级菜单
     */
    private List<MenuRouteVO> children;


    @Data
    public static class Meta {
        /**
         * 菜单标题
         */
        private String title;

        /**
         * 菜单图标
         */
        private String icon;

        /**
         * 是否隐藏侧边栏
         */
        private Boolean hidden;
    }
}