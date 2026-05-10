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


@RequiredArgsConstructor
@Service
public class SysUserTypeIdentityServiceImpl extends ServiceImpl<SysUserTypeIdentityMapper, SysUserTypeIdentity> implements SysUserTypeIdentityService {


    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;


    @Override
    public List<MenuRouteVO> getMenuRouteList(Integer identityId) {

        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SysRoleMenu::getIdentityId, identityId);
        List<Long> menuIds = sysRoleMenuMapper.selectList(queryWrapper).stream().map(SysRoleMenu::getMenuId).toList();

        // 2. 没有权限直接返回空
        if (CollUtil.isEmpty(menuIds)) {
            return null;
        }

        // 3. MP 根据菜单ID 查询所有菜单
        List<SysMenu> menuList = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .in(SysMenu::getId, menuIds)
                        .orderByAsc(SysMenu::getSort)
        );

        // 4. 构建树形
        return buildMenuTree(menuList);
    }


    @Override
    public List<MenuRouteVO> buildMenuTree(List<SysMenu> allMenuList) {
        // 最终要返回的树形菜单
        List<MenuRouteVO> result = new ArrayList<>();

        // 1. 先找出所有【一级菜单】 parent_id = 0
        List<SysMenu> rootMenus = allMenuList.stream()
                .filter(menu -> menu.getParentId() == 0)
                .toList();

        // 2. 遍历一级菜单 → 给每个菜单找二级子菜单
        for (SysMenu root : rootMenus) {
            MenuRouteVO rootVO = convert(root);

            // 找当前一级菜单的【二级菜单】
            List<MenuRouteVO> children = allMenuList.stream()
                    .filter(sub -> sub.getParentId().equals(root.getId()))
                    .map(this::convert)
                    .toList();

            // 设置子菜单
            if (!children.isEmpty()) {
                rootVO.setChildren(children);
            }
            result.add(rootVO);
        }

        return result;
    }


    private MenuRouteVO convert(SysMenu sysMenu) {
        MenuRouteVO vo = new MenuRouteVO();

        BeanUtils.copyProperties(sysMenu, vo);

        MenuRouteVO.Meta meta = new MenuRouteVO.Meta();
        meta.setTitle(sysMenu.getTitle());
        meta.setIcon(sysMenu.getIcon());
        meta.setHidden(sysMenu.getHidden() == 1);
        vo.setMeta(meta);

        return vo;
    }
}
