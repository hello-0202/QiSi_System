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
        UserTypeEnum userType = UserTypeEnum.getByCode(request.getUserType());
        if (userType == null) {
            throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        }

        switch (userType) {
            case STUDENT: {
                SchoolStudent schoolStudent = checkStudentWhitelist(request.getUsername());
                if (schoolStudent == null) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "学生信息不存在");
                }
                if (!schoolStudent.getName().equals(request.getRealName())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "真实姓名不相符");
                }
                break;
            }
            case TEACHER: {
                SchoolStaff schoolStaff = checkTeacherWhitelist(request.getUsername());
                if (schoolStaff == null) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "教师信息不存在");
                }
                if (!schoolStaff.getName().equals(request.getRealName())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "真实姓名不相符");
                }
                break;
            }
            default:
                throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        }
        // 全部校验执行完毕，没有抛出异常 = 校验通过
        return true;
    }


    /**
     * 校验学生白名单
     */
    private SchoolStudent checkStudentWhitelist(String studentId) {
        LambdaQueryWrapper<SchoolStudent> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SchoolStudent::getStudentId, studentId);
        return schoolStudentMapper.selectOne(queryWrapper);
    }


    /**
     * 校验教师白名单
     */
    private SchoolStaff checkTeacherWhitelist(String personCode) {
        LambdaQueryWrapper<SchoolStaff> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SchoolStaff::getPersonCode, personCode);
        return schoolStaffMapper.selectOne(queryWrapper);
    }
}