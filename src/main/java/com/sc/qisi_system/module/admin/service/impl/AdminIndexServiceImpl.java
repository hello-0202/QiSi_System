package com.sc.qisi_system.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.qisi_system.common.enums.UserIdentityEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.admin.dto.MenuDTO;
import com.sc.qisi_system.module.admin.dto.MenuQueryDTO;
import com.sc.qisi_system.module.admin.entity.SysMenu;
import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.mapper.SysMenuMapper;
import com.sc.qisi_system.module.admin.mapper.SysUserTypeIdentityMapper;
import com.sc.qisi_system.module.admin.service.AdminIndexService;
import com.sc.qisi_system.module.admin.service.SysUserTypeIdentityService;
import com.sc.qisi_system.module.admin.vo.MenuRouteVO;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminIndexServiceImpl implements AdminIndexService {


    private final SysMenuMapper sysMenuMapper;
    private final SysUserTypeIdentityMapper sysUserTypeIdentityMapper;
    private final SysUserService sysUserService;
    private final SysUserTypeIdentityService sysUserTypeIdentityService;


    @Override
    public UserIdentityEnum getUserIdentity() {
        SysUser sysUser = sysUserService.getById(SecurityUtils.getCurrentUserId());
        LambdaQueryWrapper<SysUserTypeIdentity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserTypeIdentity::getUserType, sysUser.getUserType());
        SysUserTypeIdentity sysUserTypeIdentity = sysUserTypeIdentityMapper.selectOne(queryWrapper);

        return UserIdentityEnum.getByCode(sysUserTypeIdentity.getIdentityId());
    }

    @Override
    public PageResult<MenuRouteVO> getRouters(MenuQueryDTO menuQueryDTO) {

        List<MenuRouteVO> tree = sysUserTypeIdentityService.getMenuRouteList(menuQueryDTO.getIdentityId());

        PageResult<MenuRouteVO> pageResult = new PageResult<>();
        pageResult.setTotal(tree.size());
        pageResult.setRecords(tree);
        pageResult.setPages(tree.size());

        return pageResult;
    }


    @Override
    public PageResult<MenuRouteVO> getMenuRouteList(MenuQueryDTO menuQueryDTO) {
        // 1. 无条件查询【所有菜单】（不分页，菜单一般不分页，直接全查构建树）
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        // 如果你要只查某个父级，就加这个条件
        if (menuQueryDTO.getParentId() != null) {
            queryWrapper.eq(SysMenu::getParentId, menuQueryDTO.getParentId());
        }
        // 排序
        queryWrapper.orderByAsc(SysMenu::getSort);

        // 2. 查询所有菜单
        List<SysMenu> allMenuList = sysMenuMapper.selectList(queryWrapper);

        // 3. 【核心】构建父子树形结构
        List<MenuRouteVO> treeList = sysUserTypeIdentityService.buildMenuTree(allMenuList);

        // 4. 封装返回（菜单一般不分页，要分页我也给你保留格式）
        PageResult<MenuRouteVO> result = new PageResult<>();
        result.setRecords(treeList);
        result.setTotal(treeList.size());
        result.setPages(1);
        return result;
    }


    @Override
    public void addMenu(MenuDTO menuDTO) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(menuDTO, sysMenu);
        sysMenuMapper.insert(sysMenu);
    }


    @Override
    public void updateMenu(MenuDTO menuDTO) {
        SysMenu sysMenu = sysMenuMapper.selectById(menuDTO.getId());
        BeanUtils.copyProperties(menuDTO, sysMenu);
        sysMenuMapper.updateById(sysMenu);
    }


    @Override
    public void deleteMenu(Long id) {
        // 1. 校验菜单是否存在
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            throw new BusinessException(ResultCode.MENU_NOT_EXIST);
        }

        // 2. 校验是否存在子菜单
        Long count = sysMenuMapper.selectCount(
                new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getParentId, id)
        );
        if (count > 0) {
            throw new BusinessException(ResultCode.MENU_HAS_CHILDREN);
        }

        // 3. 执行删除
        sysMenuMapper.deleteById(id);
    }

}
