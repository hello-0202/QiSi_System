package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import com.sc.qisi_system.module.user.entity.EduStudent;
import com.sc.qisi_system.module.user.entity.EduTeacher;
import com.sc.qisi_system.module.user.entity.EntEmployee;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.mapper.*;
import com.sc.qisi_system.module.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;


@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>  // MP 父类
        implements SysUserService {


    private final SysUserMapper sysUserMapper;
    private final EduTeacherMapper eduTeacherMapper;
    private final EduStudentMapper eduStudentMapper;
    private final EntEmployeeMapper entEmployeeMapper;


    @Override
    public boolean existsById(Long userId) {
        return getById(userId) == null;
    }


    /**
     *
     */
    @Override
    public UserProfileVO getUserProfile(Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (Objects.isNull(sysUser)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        Integer userType = sysUser.getUserType();

        UserProfileVO userProfileVO = new UserProfileVO();
        BeanUtils.copyProperties(sysUser, userProfileVO);

        return switch (userType) {
            case 1 -> getStudentDetail(sysUser, userProfileVO);
            case 2 -> getTeacherDetail(sysUser, userProfileVO);
            case 3 -> getEmployeeDetail(sysUser, userProfileVO);
            default -> userProfileVO;
        };

    }


    /**
     *
     */
    private UserProfileVO getEmployeeDetail(SysUser sysUser, UserProfileVO baseVO) {

        EntEmployee emp = entEmployeeMapper.selectOne(Wrappers
                .lambdaQuery(EntEmployee.class)
                .eq(EntEmployee::getUserId, sysUser.getId()));

        if (Objects.nonNull(emp)) BeanUtils.copyProperties(emp, baseVO);
        return baseVO;
    }


    /**
     *
     */
    private UserProfileVO getTeacherDetail(SysUser sysUser, UserProfileVO baseVO) {

        EduTeacher teacher = eduTeacherMapper.selectOne(Wrappers
                .lambdaQuery(EduTeacher.class)
                .eq(EduTeacher::getUserId, sysUser.getId()));

        if (Objects.nonNull(teacher)) BeanUtils.copyProperties(teacher, baseVO);
        return baseVO;
    }


    /**
     *
     */
    private UserProfileVO getStudentDetail(SysUser sysUser, UserProfileVO baseVO) {

        EduStudent student = eduStudentMapper.selectOne(Wrappers
                .lambdaQuery(EduStudent.class)
                .eq(EduStudent::getUserId, sysUser.getId()));

        if (Objects.nonNull(student)) BeanUtils.copyProperties(student, baseVO);
        return baseVO;
    }
}
