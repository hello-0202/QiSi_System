package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.mapper.SysUserMapper;
import com.sc.qisi_system.module.user.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>  // MP 父类
        implements SysUserService {

    @Override
    public boolean existsById(Long userId) {
        return getById(userId) != null;  // 直接用MP的方法
    }
}
