package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.enums.UserIdentityEnum;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.MenuDTO;
import com.sc.qisi_system.module.admin.dto.MenuQueryDTO;
import com.sc.qisi_system.module.admin.vo.MenuRouteVO;

public interface AdminIndexService {


    UserIdentityEnum getUserIdentity();


    PageResult<MenuRouteVO> getRouters(MenuQueryDTO menuQueryDTO);


    PageResult<MenuRouteVO> getMenuRouteList(MenuQueryDTO menuQueryDTO);


    void addMenu(MenuDTO menuDTO);


    void updateMenu(MenuDTO menuDTO);


    void deleteMenu(Long id);

}
