package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.EnterpriseRegisterDTO;
import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterDTO;


/**
 * 注册业务服务接口
 * 提供学生、教师、企业用户的注册功能
 */
public interface RegisterService {


    /**
     * 学生/教师统一注册
     *
     * @param request 学生教师注册参数
     */
    void registerStudentTeacher(StudentTeacherRegisterDTO request);


    /**
     * 企业人员注册
     *
     * @param request 企业人员注册参数
     */
    void registerEnterprise(EnterpriseRegisterDTO request);
}