package com.sc.qisi_system.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.user.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    boolean existsById(Long userId);
}
