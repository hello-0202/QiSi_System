package com.sc.qisi_system.module.user.dto;

import lombok.Data;

@Data
public class StudentTeacherRegisterDTO {


    /**
     * 用户名: 学生-学号 教师-工号
     */
    private String username;


    /**
     * 密码
     */
    private String password;


    /**
     * 姓名
     */
    private String name;


    /**
     * 手机号
     */
    private String phone;


    /**
     * 身份证号
     */
    private String email;


    /**
     * 用户类型: 1-学生 2-教师
     */
    private Integer userType;


    /**
     * 验证码唯一标识码
     */
    private String captchaKey;


    /**
     * 验证码
     */
    private String captchaCode;


}
