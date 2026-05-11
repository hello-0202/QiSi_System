package com.sc.qisi_system.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.admin.entity.SysMenu;
import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.vo.MenuRouteVO;

import java.util.List;


/**
 * 用户类型与业务身份关联服务接口
 * 功能: 根据身份ID获取菜单路由、构建菜单树形结构等权限菜单业务逻辑
 */
public interface SysUserTypeIdentityService extends IService<SysUserTypeIdentity> {


    /**
     * 根据业务身份ID获取关联菜单路由列表
     *
     * @param identityId 业务身份ID
     * @return 菜单路由VO列表
     */
    List<MenuRouteVO> getMenuRouteList(Integer identityId);


    /**
     * 构建菜单树形结构
     *
     * @param allMenuList 所有菜单原始列表
     * @return 树形结构菜单路由列表
     */
    List<MenuRouteVO> buildMenuTree(List<SysMenu> allMenuList);
}