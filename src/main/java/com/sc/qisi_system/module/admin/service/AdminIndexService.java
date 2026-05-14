package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.enums.UserIdentityEnum;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.MenuDTO;
import com.sc.qisi_system.module.admin.dto.MenuQueryDTO;
import com.sc.qisi_system.module.admin.vo.MenuRouteVO;


/**
 * 管理员首页与菜单管理服务接口
 * 功能: 获取用户业务身份、动态路由查询、菜单列表查询、菜单新增/修改/删除业务逻辑
 */
public interface AdminIndexService {


    /**
     * 获取当前登录用户业务身份
     * 角色: 管理员
     *
     * @return 用户身份枚举
     */
    UserIdentityEnum getUserIdentity();


    /**
     * 按角色获取动态路由列表
     * 角色: 管理员
     *
     * @param menuQueryDTO 菜单路由查询条件
     * @return 路由分页列表
     */
    PageResult<MenuRouteVO> getRouters(MenuQueryDTO menuQueryDTO);


    /**
     * 条件查询所有菜单路由列表
     * 角色: 管理员
     *
     * @param menuQueryDTO 菜单查询条件
     * @return 菜单路由分页列表
     */
    PageResult<MenuRouteVO> getMenuRouteList(MenuQueryDTO menuQueryDTO);


    /**
     * 添加菜单与业务身份绑定
     * 角色: 管理员
     *
     * @param menuId 菜单ID
     * @param identityId 业务身份ID
     */
    void bindMenuIdentity(Long menuId, Long identityId);


    /**
     * 将菜单与业务身份解绑接口
     * 角色: 管理员
     *
     * @param id 主键id
     */
    void unbindMenuIdentity(Long id);


    /**
     * 新增系统菜单
     * 角色: 管理员
     *
     * @param menuDTO 菜单新增参数
     */
    void addMenu(MenuDTO menuDTO);


    /**
     * 修改系统菜单
     * 角色: 管理员
     *
     * @param menuDTO 菜单修改参数
     */
    void updateMenu(MenuDTO menuDTO);


    /**
     * 删除系统菜单
     * 角色: 管理员
     *
     * @param id 菜单主键ID
     */
    void deleteMenu(Long id);
}