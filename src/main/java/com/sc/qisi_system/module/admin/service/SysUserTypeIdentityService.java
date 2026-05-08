package com.sc.qisi_system.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.admin.entity.SysMenu;
import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.vo.MenuRouteVO;

import java.util.List;

public interface SysUserTypeIdentityService extends IService<SysUserTypeIdentity> {

    List<MenuRouteVO> getMenuRouteList(Integer identityId);

    List<MenuRouteVO> buildMenuTree(List<SysMenu> allMenuList);
}
