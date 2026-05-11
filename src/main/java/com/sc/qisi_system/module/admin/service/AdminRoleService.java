package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.module.admin.vo.SysUserTypeIdentityVO;

import java.util.List;


/**
 * 管理员角色权限服务接口
 * 功能: 用户类型与业务身份映射关系查询、批量修改保存等业务逻辑
 */
public interface AdminRoleService {


    /**
     * 查询用户类型与业务身份映射关系
     * 角色: 管理员
     *
     * @return 映射关系数据列表
     */
    List<SysUserTypeIdentityVO> getIdentityMap();


    /**
     * 批量修改/保存用户类型与业务身份映射关系
     * 角色: 管理员
     *
     * @param voList 映射关系数据列表
     */
    void updateIdentityMap(List<SysUserTypeIdentityVO> voList);
}