package com.sc.qisi_system.module.user.service.impl;


import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.mapper.SysUserMapper;
import com.sc.qisi_system.module.user.service.SysUserService;
import com.sc.qisi_system.module.user.service.UserInfoService;
import com.sc.qisi_system.module.user.vo.UserInfoVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final SysUserMapper sysUserMapper;
    private final SysUserService sysUserService;


    @Override
    public UserInfoVO getUserInfo() {
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getCurrentUserId());
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(sysUser, userInfoVO);
        return userInfoVO;
    }


    @Override
    public UserProfileVO getUserProfile() {
        return sysUserService.getUserProfile(SecurityUtils.getCurrentUserId());
    }
}
