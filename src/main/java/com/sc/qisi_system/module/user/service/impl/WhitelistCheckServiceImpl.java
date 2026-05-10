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

@RequiredArgsConstructor
@Service
public class WhitelistCheckServiceImpl implements WhitelistCheckService {

    private final SchoolStudentMapper schoolStudentMapper;

    private final SchoolStaffMapper schoolStaffMapper;

    public boolean isInWhitelist(StudentTeacherRegisterDTO request) {

        UserTypeEnum userType = UserTypeEnum.getByCode(request.getUserType());

        if (userType == null) {
            throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        }

        return switch (userType) {
            case STUDENT ->
                    checkStudentWhitelist(request.getUsername());
            case TEACHER ->
                    checkTeacherWhitelist(request.getUsername());
            default -> throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        };
    }

    private boolean checkStudentWhitelist(String studentId) {
        LambdaQueryWrapper<SchoolStudent> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SchoolStudent::getStudentId, studentId);
        return schoolStudentMapper.selectCount(queryWrapper) > 0;
    }

    private boolean checkTeacherWhitelist(String personCode) {
        LambdaQueryWrapper<SchoolStaff> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SchoolStaff::getPersonCode, personCode);
        return schoolStaffMapper.selectCount(queryWrapper) > 0;
    }
}
