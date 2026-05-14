package com.sc.qisi_system.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.enums.UserIdentityEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.admin.dto.MenuDTO;
import com.sc.qisi_system.module.admin.dto.MenuQueryDTO;
import com.sc.qisi_system.module.admin.entity.SysMenu;
import com.sc.qisi_system.module.admin.entity.SysRoleMenu;
import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.mapper.SysMenuMapper;
import com.sc.qisi_system.module.admin.mapper.SysRoleMenuMapper;
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


/**
 * 管理员首页与菜单服务实现类
 */
@RequiredArgsConstructor
@Service
public class AdminIndexServiceImpl implements AdminIndexService {


    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserTypeIdentityMapper sysUserTypeIdentityMapper;
    private final SysUserService sysUserService;
    private final SysUserTypeIdentityService sysUserTypeIdentityService;


    /**
     * 获取当前用户业务身份
     */
    @Override
    public UserIdentityEnum getUserIdentity() {
        // 1. 获取当前登录用户信息
        SysUser sysUser = sysUserService.getById(SecurityUtils.getCurrentUserId());

        // 2. 根据用户类型查询身份配置
        LambdaQueryWrapper<SysUserTypeIdentity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserTypeIdentity::getUserType, sysUser.getUserType());
        SysUserTypeIdentity sysUserTypeIdentity = sysUserTypeIdentityMapper.selectOne(queryWrapper);

        // 3. 转换并返回身份枚举
        return UserIdentityEnum.getByCode(sysUserTypeIdentity.getIdentityId());
    }


    /**
     * 根据身份ID获取路由菜单
     */
    @Override
    public PageResult<MenuRouteVO> getRouters(MenuQueryDTO menuQueryDTO) {
        // 1. 根据身份ID获取菜单路由
        List<MenuRouteVO> tree = sysUserTypeIdentityService.getMenuRouteList(menuQueryDTO.getIdentityId());

        // 2. 封装分页结果返回
        PageResult<MenuRouteVO> pageResult = new PageResult<>();
        pageResult.setTotal(tree.size());
        pageResult.setRecords(tree);
        pageResult.setPages(tree.size());

        return pageResult;
    }


    /**
     * 获取菜单路由树形列表
     */
    @Override
    public PageResult<MenuRouteVO> getMenuRouteList(MenuQueryDTO menuQueryDTO) {
        // 1. 构建查询条件
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        if (menuQueryDTO.getParentId() != null) {
            queryWrapper.eq(SysMenu::getParentId, menuQueryDTO.getParentId());
        }
        queryWrapper.orderByAsc(SysMenu::getSort);

        // 2. 查询所有菜单数据
        List<SysMenu> allMenuList = sysMenuMapper.selectList(queryWrapper);

        // 3. 构建菜单树形结构
        List<MenuRouteVO> treeList = sysUserTypeIdentityService.buildMenuTree(allMenuList);

        // 4. 封装并返回结果
        PageResult<MenuRouteVO> result = new PageResult<>();
        result.setRecords(treeList);
        result.setTotal(treeList.size());
        result.setPages(1);
        return result;
    }


    @Override
    public void bindMenuIdentity(Long menuId, Long identityId) {
        if(!sysMenuMapper.exists(Wrappers.lambdaQuery(SysMenu.class).eq(SysMenu::getParentId, menuId))){
            throw new BusinessException(ResultCode.MENU_NOT_EXIST);
        }
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setIdentityId(identityId);
        sysRoleMenu.setMenuId(menuId);
        sysRoleMenuMapper.insert(sysRoleMenu);
    }


    @Override
    public void unbindMenuIdentity(Long id) {
        sysRoleMenuMapper.deleteById(id);
    }


    /**
     * 新增菜单
     */
    @Override
    public void addMenu(MenuDTO menuDTO) {
        // 1. 复制属性到实体
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(menuDTO, sysMenu);

        // 2. 执行新增
        sysMenuMapper.insert(sysMenu);
    }


    /**
     * 修改菜单
     */
    @Override
    public void updateMenu(MenuDTO menuDTO) {
        // 1. 查询原菜单信息
        SysMenu sysMenu = sysMenuMapper.selectById(menuDTO.getId());

        // 2. 复制新属性
        BeanUtils.copyProperties(menuDTO, sysMenu);

        // 3. 执行更新
        sysMenuMapper.updateById(sysMenu);
    }


    /**
     * 删除菜单
     */
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