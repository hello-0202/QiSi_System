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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final SysUserMapper sysUserMapper;
    private final SysUserService sysUserService;
    private final MinioService minioService;


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


    @Override
    public void updateUserInfo(UserInfoDTO userInfoDTO) {
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getCurrentUserId());
        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        sysUser.setPassword(userInfoDTO.getPassword());
        sysUser.setPhone(userInfoDTO.getPhone());
        sysUser.setEmail(userInfoDTO.getEmail());
        sysUserMapper.updateById(sysUser);
    }


    @Override
    public void updateAvatar(MultipartFile file) {
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getCurrentUserId());
        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        sysUser.setAvatar(minioService.updateUserAvatar(SecurityUtils.getCurrentUserId(), file));
        sysUserMapper.updateById(sysUser);
    }
}
