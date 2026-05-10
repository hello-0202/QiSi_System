package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.user.dto.EnterpriseRegisterDTO;
import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterDTO;
import com.sc.qisi_system.module.user.entity.*;
import com.sc.qisi_system.module.user.mapper.*;
import com.sc.qisi_system.module.user.service.CaptchaService;
import com.sc.qisi_system.module.user.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class RegisterServiceImpl implements RegisterService {


    private final SysUserMapper sysUserMapper;
    private final WhitelistCheckServiceImpl whitelistCheckServiceImpl;
    private final CaptchaService captchaService;
    private final PasswordEncoder passwordEncoder;
    private final SchoolStudentMapper schoolStudentMapper;
    private final SchoolStaffMapper schoolStaffMapper;
    private final EduStudentMapper eduStudentMapper;
    private final EduTeacherMapper eduTeacherMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerStudentTeacher(StudentTeacherRegisterDTO request) {

        captchaService.checkCaptcha(request.getCaptchaKey(), request.getCaptchaCode());

        if (!whitelistCheckServiceImpl.isInWhitelist(request)) {
            throw new BusinessException(ResultCode.WHITELIST_NOT_FOUND);
        }

        Long count = sysUserMapper.selectCount(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getUsername,request.getUsername()));
        if (count > 0) {
            throw new BusinessException(ResultCode.NAME_DUPLICATE);
        }

        SysUser sysUser = SysUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .email(request.getEmail())
                .avatar(null)
                .userType(request.getUserType())
                .build();


        sysUserMapper.insert(sysUser);
        handleWhitelistDataByUserType(sysUser);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerEnterprise(EnterpriseRegisterDTO request) {

        captchaService.checkCaptcha(request.getCaptchaKey(), request.getCaptchaCode());

        if (!UserTypeEnum.COMPANY.getCode().equals(request.getUserType())) {
            throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        }

        Long count = sysUserMapper.selectCount(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException(ResultCode.NAME_DUPLICATE);
        }

        SysUser sysUser = SysUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getUsername())
                .email(request.getEmail())
                .avatar(null)
                .userType(request.getUserType())
                .build();

        sysUserMapper.insert(sysUser);
        handleWhitelistDataByUserType(sysUser);
    }


    private void handleWhitelistDataByUserType(SysUser sysUser) {
        switch (sysUser.getUserType()) {
            case 1 -> handleTeacherWhitelist(sysUser);
            case 2 -> handleStudentWhitelist(sysUser);
            case 3 -> handleEntEmployeeWhitelist();
        }
    }


    /**
     * 处理教师白名单数据
     */
    private void handleTeacherWhitelist(SysUser sysUser) {
        LambdaQueryWrapper<SchoolStaff> queryWrapper = Wrappers.lambdaQuery(SchoolStaff.class);
        queryWrapper.eq(SchoolStaff::getPersonCode,sysUser.getUsername());
        SchoolStaff schoolStaff = schoolStaffMapper.selectOne(queryWrapper);
        EduTeacher eduTeacher = new EduTeacher();
        BeanUtils.copyProperties(schoolStaff,eduTeacher);
        eduTeacherMapper.insert(eduTeacher);
    }


    /**
     * 处理学生白名单数据
     */
    private void handleStudentWhitelist(SysUser sysUser) {
        LambdaQueryWrapper<SchoolStudent> queryWrapper = Wrappers.lambdaQuery(SchoolStudent.class);
        queryWrapper.eq(SchoolStudent::getStudentId,sysUser.getUsername());
        SchoolStudent schoolStudent = schoolStudentMapper.selectOne(queryWrapper);
        EduStudent eduStudent = new EduStudent();
        BeanUtils.copyProperties(schoolStudent, eduStudent, "createTime", "updateTime");
        eduStudent.setUserId(sysUser.getId());
        eduStudentMapper.insert(eduStudent);
    }


    /**
     * 处理企业人员白名单数据
     */
    private void handleEntEmployeeWhitelist() {
    }
}
