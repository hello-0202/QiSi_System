package com.sc.qisi_system.module.admin.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.admin.entity.SysMenu;
import com.sc.qisi_system.module.admin.entity.SysRoleMenu;
import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.mapper.SysMenuMapper;
import com.sc.qisi_system.module.admin.mapper.SysRoleMenuMapper;
import com.sc.qisi_system.module.admin.mapper.SysUserTypeIdentityMapper;
import com.sc.qisi_system.module.admin.service.SysUserTypeIdentityService;
import com.sc.qisi_system.module.admin.vo.MenuRouteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户类型与身份关联服务实现类
 */
@RequiredArgsConstructor
@Service
public class SysUserTypeIdentityServiceImpl extends ServiceImpl<SysUserTypeIdentityMapper, SysUserTypeIdentity> implements SysUserTypeIdentityService {


    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;


    /**
     * 根据业务身份ID获取菜单路由列表
     */
    @Override
    public List<MenuRouteVO> getMenuRouteList(Integer identityId) {
        // 1. 根据身份ID查询关联的菜单ID集合
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getIdentityId, identityId);
        List<Long> menuIds = sysRoleMenuMapper.selectList(queryWrapper).stream()
                .map(SysRoleMenu::getMenuId)
                .toList();

        // 2. 无权限菜单直接返回空
        if (CollUtil.isEmpty(menuIds)) {
            return null;
        }

        // 3. 根据菜单ID查询菜单详情并排序
        List<SysMenu> menuList = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .in(SysMenu::getId, menuIds)
                        .orderByAsc(SysMenu::getSort)
        );

        // 4. 构建菜单树形结构并返回
        return buildMenuTree(menuList);
    }


    /**
     * 构建菜单树形结构
     */
    @Override
    public List<MenuRouteVO> buildMenuTree(List<SysMenu> allMenuList) {

        List<MenuRouteVO> result = new ArrayList<>();

        // 1. 筛选出一级菜单（父ID为0）
        List<SysMenu> rootMenus = allMenuList.stream()
                .filter(menu -> menu.getParentId() == 0)
                .toList();

        // 2. 遍历一级菜单，递归设置子菜单
        for (SysMenu root : rootMenus) {
            MenuRouteVO rootVO = convert(root);

            // 3. 查找当前菜单的子菜单
            List<MenuRouteVO> children = allMenuList.stream()
                    .filter(sub -> sub.getParentId().equals(root.getId()))
                    .map(this::convert)
                    .toList();

            // 4. 设置子菜单
            if (!children.isEmpty()) {
                rootVO.setChildren(children);
            }
            result.add(rootVO);
        }

        return result;
    }


    /**
     * 菜单实体转换为路由VO
     */
    private MenuRouteVO convert(SysMenu sysMenu) {
        // 1. 基础属性拷贝
        MenuRouteVO vo = new MenuRouteVO();
        BeanUtils.copyProperties(sysMenu, vo);

        // 2. 设置路由元信息
        MenuRouteVO.Meta meta = new MenuRouteVO.Meta();
        meta.setTitle(sysMenu.getTitle());
        meta.setIcon(sysMenu.getIcon());
        meta.setHidden(sysMenu.getHidden() == 1);
        vo.setMeta(meta);

        return vo;
    }
}