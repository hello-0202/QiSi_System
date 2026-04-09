package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.EnterpriseRegisterRequest;
import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterRequest;
import com.sc.qisi_system.module.user.service.impl.RegisterServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user/register")
@RequiredArgsConstructor
@RestController
public class RegisterController {

    private final RegisterServiceImpl registerServiceimpl;

    /**
     * 学生/教师注册
     */
    @PostMapping("/student-teacher")
    public Result registerStudentTeacher(@Validated @RequestBody StudentTeacherRegisterRequest request) {
        return registerServiceimpl.registerStudentTeacher(request);
    }

    /**
     * 企业人员注册
     */
    @PostMapping("/enterprise")
    public Result registerEnterprise(@Validated @RequestBody EnterpriseRegisterRequest request) {
        return registerServiceimpl.registerEnterprise(request);
    }
}
