package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.SysUserQueryDTO;
import com.sc.qisi_system.module.admin.vo.SysUserVO;
import com.sc.qisi_system.module.user.dto.SysUserResetPasswordDTO;

public interface AdminUserService {


    PageResult<SysUserVO> getUserList(SysUserQueryDTO sysUserQueryDTO);


    void resetUserPassword(SysUserResetPasswordDTO dto);


    void banUser(Long userId);


    void unbanUser(Long userId);
}
