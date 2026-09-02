package com.sc.qisi_system.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudentTeacherRegisterDTO {


    /**
     * 真实姓名
     */
    @NotBlank
    private String realName;

    /**
     * 用户名: 学生-学号 教师-工号
     */
    private String username;


    /**
     * 密码
     */
    private String password;


    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;


    /**
     * 邮箱
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
