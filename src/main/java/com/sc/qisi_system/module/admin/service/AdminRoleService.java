package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.module.admin.vo.SysUserTypeIdentityVO;

import java.util.List;

public interface AdminRoleService {


    List<SysUserTypeIdentityVO> getIdentityMap();


    void updateIdentityMap(List<SysUserTypeIdentityVO> voList);
}
