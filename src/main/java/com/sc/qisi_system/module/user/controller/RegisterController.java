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
@Validated
public class RegisterController {

    private final RegisterServiceImpl registerServiceimpl;

    /**
     * 学生/教师注册接口
     * @param request 学生/教师注册请求参数
     * @return 成功相关信息，失败返回错误信息
     */
    @PostMapping("/student-teacher")
    public Result registerStudentTeacher(@RequestBody StudentTeacherRegisterRequest request) {
        return registerServiceimpl.registerStudentTeacher(request);
    }

    /**
     * 企业人员注册接口
     * @param request 企业人员请求参数
     * @return 成功返回相关信息，失败返回错误信息
     */
    @PostMapping("/enterprise")
    public Result registerEnterprise(@RequestBody EnterpriseRegisterRequest request) {
        return registerServiceimpl.registerEnterprise(request);
    }
}
