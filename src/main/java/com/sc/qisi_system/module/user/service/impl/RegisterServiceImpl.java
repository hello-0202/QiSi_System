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


/**
 * 注册服务实现类
 */
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


    /**
     * 学生/教师注册
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerStudentTeacher(StudentTeacherRegisterDTO request) {
        // 1. 校验验证码
        captchaService.checkCaptcha(request.getCaptchaKey(), request.getCaptchaCode());

        // 2. 校验白名单权限
        if (!whitelistCheckServiceImpl.isInWhitelist(request)) {
            throw new BusinessException(ResultCode.WHITELIST_NOT_FOUND);
        }

        // 3. 校验用户名是否重复
        Long count = sysUserMapper.selectCount(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getUsername,request.getUsername()));
        if (count > 0) {
            throw new BusinessException(ResultCode.NAME_DUPLICATE);
        }

        // 4. 构建并保存用户信息
        SysUser sysUser = SysUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .email(request.getEmail())
                .avatar(null)
                .userType(request.getUserType())
                .build();

        sysUserMapper.insert(sysUser);

        // 5. 根据用户类型处理白名单数据
        handleWhitelistDataByUserType(sysUser);
    }


    /**
     * 企业用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerEnterprise(EnterpriseRegisterDTO request) {
        // 1. 校验验证码
        captchaService.checkCaptcha(request.getCaptchaKey(), request.getCaptchaCode());

        // 2. 校验用户类型是否为企业
        if (!UserTypeEnum.COMPANY.getCode().equals(request.getUserType())) {
            throw new BusinessException(ResultCode.USER_TYPE_ERROR);
        }

        // 3. 校验用户名是否重复
        Long count = sysUserMapper.selectCount(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException(ResultCode.NAME_DUPLICATE);
        }

        // 4. 构建并保存企业用户信息
        SysUser sysUser = SysUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getUsername())
                .email(request.getEmail())
                .avatar(null)
                .userType(request.getUserType())
                .build();

        sysUserMapper.insert(sysUser);

        // 5. 处理企业白名单数据
        handleWhitelistDataByUserType(sysUser);
    }


    /**
     * 根据用户类型分发白名单处理逻辑
     */
    private void handleWhitelistDataByUserType(SysUser sysUser) {
        switch (sysUser.getUserType()) {
            case 1 -> handleStudentWhitelist(sysUser);
            case 2 -> handleTeacherWhitelist(sysUser);
            case 3 -> handleEntEmployeeWhitelist();
        }
    }


    /**
     * 处理教师白名单数据
     */
    private void handleTeacherWhitelist(SysUser sysUser) {
        // 1. 查询教师白名单
        LambdaQueryWrapper<SchoolStaff> queryWrapper = Wrappers.lambdaQuery(SchoolStaff.class);
        queryWrapper.eq(SchoolStaff::getPersonCode,sysUser.getUsername());
        SchoolStaff schoolStaff = schoolStaffMapper.selectOne(queryWrapper);

        if (schoolStaff == null) {
            throw new BusinessException(ResultCode.TEACHER_WHITELIST_NOT_EXIST);
        }

        // 2. 同步数据到教师正式表
        EduTeacher eduTeacher = new EduTeacher();
        BeanUtils.copyProperties(schoolStaff,eduTeacher);
        eduTeacher.setTeacherNo(schoolStaff.getPersonCode());
        eduTeacher.setUserId(sysUser.getId());

        // 3. 更新用户姓名
        sysUser.setName(schoolStaff.getName());
        sysUserMapper.updateById(sysUser);

        // 4. 保存教师信息
        eduTeacherMapper.insert(eduTeacher);
    }


    /**
     * 处理学生白名单数据
     */
    private void handleStudentWhitelist(SysUser sysUser) {
        // 1. 查询学生白名单
        LambdaQueryWrapper<SchoolStudent> queryWrapper = Wrappers.lambdaQuery(SchoolStudent.class);
        queryWrapper.eq(SchoolStudent::getStudentId,sysUser.getUsername());
        SchoolStudent schoolStudent = schoolStudentMapper.selectOne(queryWrapper);

        if(schoolStudent == null){
            throw new BusinessException(ResultCode.STUDENT_WHITELIST_NOT_EXIST);
        }

        // 2. 同步数据到学生正式表
        EduStudent eduStudent = new EduStudent();
        BeanUtils.copyProperties(schoolStudent, eduStudent, "createTime", "updateTime");
        eduStudent.setStudentNo(schoolStudent.getStudentId());
        eduStudent.setUserId(sysUser.getId());

        // 3. 更新用户姓名
        sysUser.setName(schoolStudent.getName());
        sysUserMapper.updateById(sysUser);

        // 4. 保存学生信息
        eduStudentMapper.insert(eduStudent);
    }

    /**
     * 处理企业人员白名单数据（暂未实现）
     */
    private void handleEntEmployeeWhitelist() {
    }
}