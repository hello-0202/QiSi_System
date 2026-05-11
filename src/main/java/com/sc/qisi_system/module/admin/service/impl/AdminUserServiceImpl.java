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


/**
 * 管理员用户管理服务实现类
 */
@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {


    private final SysUserService sysUserService;
    private final WebSocketMessageService webSocketMessageService;
    private final PasswordEncoder passwordEncoder;


    /**
     * 条件查询用户列表
     */
    @Override
    public PageResult<SysUserVO> getUserList(SysUserQueryDTO sysUserQueryDTO) {
        // 1. 构建分页对象
        Page<SysUser> sysUserPage = new Page<>(sysUserQueryDTO.getPage(), sysUserQueryDTO.getPageSize());

        // 2. 构建动态查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(sysUserQueryDTO.getUsername() != null, SysUser::getUsername, sysUserQueryDTO.getUsername())
                .eq(sysUserQueryDTO.getName() != null, SysUser::getName, sysUserQueryDTO.getName())
                .eq(sysUserQueryDTO.getPhone() != null, SysUser::getPhone, sysUserQueryDTO.getPhone())
                .eq(sysUserQueryDTO.getUserType() != null, SysUser::getUserType, sysUserQueryDTO.getUserType())
                .eq(sysUserQueryDTO.getStatus() != null, SysUser::getStatus, sysUserQueryDTO.getStatus());

        // 3. 分页查询用户数据
        IPage<SysUser> sysUserIPage = sysUserService.page(sysUserPage, queryWrapper);

        // 4. 转换为VO对象
        List<SysUserVO> sysUserVOList = sysUserIPage.getRecords().stream().map(sysUser -> {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            sysUserVO.setUserProfileVO(sysUserService.getUserProfile(sysUser.getId()));
            return sysUserVO;
        }).toList();

        // 5. 封装分页结果返回
        PageResult<SysUserVO> pageResult = new PageResult<>();
        pageResult.setRecords(sysUserVOList);
        pageResult.setTotal(sysUserIPage.getTotal());
        pageResult.setPages(sysUserIPage.getPages());

        return pageResult;
    }


    /**
     * 管理员重置用户密码
     */
    @Override
    public void resetUserPassword(SysUserResetPasswordDTO dto) {
        // 1. 校验用户是否存在
        SysUser sysUser = sysUserService.getById(dto.getUserId());
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 密码加密
        String encodePassword = passwordEncoder.encode(dto.getNewPassword());

        // 3. 更新用户密码
        SysUser updateUser = new SysUser();
        updateUser.setId(dto.getUserId());
        updateUser.setPassword(encodePassword);
        sysUserService.updateById(updateUser);
    }


    /**
     * 封禁用户
     */
    @Override
    public void banUser(Long userId) {
        // 1. 校验用户是否存在
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 设置用户状态为禁用
        sysUser.setStatus(false);
        sysUserService.updateById(sysUser);

        // 3. 发送封禁下线通知
        webSocketMessageService.sendKickOffNotice(userId, KickOffReasonEnum.BANNED_BY_ADMIN.getReason());
    }


    /**
     * 解除用户封禁
     */
    @Override
    public void unbanUser(Long userId) {
        // 1. 校验用户是否存在
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 设置用户状态为启用
        sysUser.setStatus(true);
        sysUserService.updateById(sysUser);

        // 3. 发送解封通知
        webSocketMessageService.sendKickOffNotice(userId, KickOffReasonEnum.UNBANNED.getReason());
    }
}