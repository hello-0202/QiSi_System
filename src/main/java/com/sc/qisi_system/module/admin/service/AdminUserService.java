package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.SysUserQueryDTO;
import com.sc.qisi_system.module.admin.vo.SysUserVO;
import com.sc.qisi_system.module.user.dto.SysUserResetPasswordDTO;

/**
 * 管理员用户管理服务接口
 * 功能: 用户列表查询、用户密码重置、用户封禁与解封等用户管理业务逻辑
 */
public interface AdminUserService {


    /**
     * 条件查询用户列表
     * 角色: 管理员
     *
     * @param sysUserQueryDTO 用户查询条件
     * @return 用户分页列表
     */
    PageResult<SysUserVO> getUserList(SysUserQueryDTO sysUserQueryDTO);


    /**
     * 管理员重置用户密码
     * 角色: 管理员
     *
     * @param dto 密码重置参数
     */
    void resetUserPassword(SysUserResetPasswordDTO dto);


    /**
     * 封禁用户
     * 角色: 管理员
     *
     * @param userId 用户ID
     */
    void banUser(Long userId);


    /**
     * 解除用户封禁
     * 角色: 管理员
     *
     * @param userId 用户ID
     */
    void unbanUser(Long userId);
}