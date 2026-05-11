package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterDTO;
import com.sc.qisi_system.module.user.entity.SchoolStaff;
import com.sc.qisi_system.module.user.entity.SchoolStudent;
import com.sc.qisi_system.module.user.mapper.SchoolStaffMapper;
import com.sc.qisi_system.module.user.mapper.SchoolStudentMapper;
import com.sc.qisi_system.module.user.service.WhitelistCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 白名单校验服务实现类
 */
@RequiredArgsConstructor
@Service
public class WhitelistCheckServiceImpl implements WhitelistCheckService {


    private final SchoolStudentMapper schoolStudentMapper;
    private final SchoolStaffMapper schoolStaffMapper;


    /**
     * 校验用户是否在白名单中
     */
    @Override
    public boolean isInWhitelist(StudentTeacherRegisterDTO request) {
        // 1. 获取用户类型
        UserTypeEnum userType = UserTypeEnum.getByCode(request.getUserType());

        if (userType == null) {
            throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        }

        // 2. 根据用户类型校验白名单
        return switch (userType) {
            case STUDENT ->
                    checkStudentWhitelist(request.getUsername());
            case TEACHER ->
                    checkTeacherWhitelist(request.getUsername());
            default -> throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        };
    }


    /**
     * 校验学生白名单
     */
    private boolean checkStudentWhitelist(String studentId) {
        LambdaQueryWrapper<SchoolStudent> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SchoolStudent::getStudentId, studentId);
        return schoolStudentMapper.selectCount(queryWrapper) > 0;
    }


    /**
     * 校验教师白名单
     */
    private boolean checkTeacherWhitelist(String personCode) {
        LambdaQueryWrapper<SchoolStaff> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SchoolStaff::getPersonCode, personCode);
        return schoolStaffMapper.selectCount(queryWrapper) > 0;
    }
}