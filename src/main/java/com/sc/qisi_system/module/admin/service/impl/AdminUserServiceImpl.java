package com.sc.qisi_system.module.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.enums.KickOffReasonEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.admin.dto.SysUserQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminUserService;
import com.sc.qisi_system.module.admin.vo.SysUserVO;
import com.sc.qisi_system.module.user.dto.SysUserResetPasswordDTO;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.SysUserService;
import com.sc.qisi_system.module.websocket.service.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {


    private final SysUserService sysUserService;
    private final WebSocketMessageService webSocketMessageService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public PageResult<SysUserVO> getUserList(SysUserQueryDTO sysUserQueryDTO) {

        Page<SysUser> sysUserPage = new Page<>(sysUserQueryDTO.getPage(),sysUserQueryDTO.getPageSize());
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(sysUserQueryDTO.getUsername() != null, SysUser::getUsername, sysUserQueryDTO.getUsername())
                .eq(sysUserQueryDTO.getName() != null, SysUser::getName, sysUserQueryDTO.getName())
                .eq(sysUserQueryDTO.getPhone() != null, SysUser::getPhone, sysUserQueryDTO.getPhone())
                .eq(sysUserQueryDTO.getUserType() != null, SysUser::getUserType, sysUserQueryDTO.getUserType())
                .eq(sysUserQueryDTO.getStatus() != null, SysUser::getStatus, sysUserQueryDTO.getStatus());
        IPage<SysUser> sysUserIPage = sysUserService.page(sysUserPage, queryWrapper);
        List<SysUserVO> sysUserVOList = sysUserIPage.getRecords().stream().map(
                sysUser -> {
                    SysUserVO sysUserVO = new SysUserVO();
                    BeanUtils.copyProperties(sysUser, sysUserVO);
                    sysUserVO.setUserProfileVO(sysUserService.getUserProfile(sysUser.getId()));
                    return sysUserVO;
                }).toList();

        PageResult<SysUserVO> pageResult = new PageResult<>();
        pageResult.setRecords(sysUserVOList);
        pageResult.setTotal(sysUserIPage.getTotal());
        pageResult.setPages(sysUserIPage.getPages());

        return pageResult;
    }


    @Override
    public void resetUserPassword(SysUserResetPasswordDTO dto) {

        SysUser sysUser = sysUserService.getById(dto.getUserId());
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        String encodePassword = passwordEncoder.encode(dto.getNewPassword());
        SysUser updateUser = new SysUser();
        updateUser.setId(dto.getUserId());
        updateUser.setPassword(encodePassword);

        sysUserService.updateById(updateUser);
    }


    @Override
    public void banUser(Long userId) {
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        sysUser.setStatus(false);
        sysUserService.updateById(sysUser);
        webSocketMessageService.sendKickOffNotice(userId, KickOffReasonEnum.BANNED_BY_ADMIN.getReason());
    }


    @Override
    public void unbanUser(Long userId) {
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        sysUser.setStatus(true);
        sysUserService.updateById(sysUser);
        webSocketMessageService.sendKickOffNotice(userId, KickOffReasonEnum.UNBANNED.getReason());
    }
}
