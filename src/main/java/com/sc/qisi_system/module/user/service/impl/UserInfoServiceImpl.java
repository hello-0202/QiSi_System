package com.sc.qisi_system.module.user.service.impl;


import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.user.dto.UserInfoDTO;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.mapper.SysUserMapper;
import com.sc.qisi_system.module.user.service.SysUserService;
import com.sc.qisi_system.module.user.service.UserInfoService;
import com.sc.qisi_system.module.user.vo.UserInfoVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户信息服务实现类
 */
@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements UserInfoService {


    private final SysUserMapper sysUserMapper;
    private final SysUserService sysUserService;
    private final MinioService minioService;
    private final PasswordEncoder passwordEncoder;


    /**
     * 获取当前用户详细信息
     */
    @Override
    public UserInfoVO getUserInfo() {
        // 1. 获取当前登录用户ID
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getCurrentUserId());
        // 2. 转换为VO返回
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(sysUser, userInfoVO);
        return userInfoVO;
    }


    /**
     * 获取用户基础展示信息
     */
    @Override
    public UserProfileVO getUserProfile() {
        return sysUserService.getUserProfile(SecurityUtils.getCurrentUserId());
    }


    /**
     * 修改用户信息（密码、手机号、邮箱）
     */
    @Override
    public void updateUserInfo(UserInfoDTO userInfoDTO) {
        // 1. 校验用户是否存在
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getCurrentUserId());
        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 2. 更新用户信息
        sysUser.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));
        sysUser.setPhone(userInfoDTO.getPhone());
        sysUser.setEmail(userInfoDTO.getEmail());
        sysUserMapper.updateById(sysUser);
    }


    /**
     * 更新用户头像
     */
    @Override
    public void updateAvatar(MultipartFile file) {
        // 1. 校验用户是否存在
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getCurrentUserId());
        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 2. 上传头像并更新用户信息
        sysUser.setAvatar(minioService.updateUserAvatar(SecurityUtils.getCurrentUserId(), file));
        sysUserMapper.updateById(sysUser);
    }
}