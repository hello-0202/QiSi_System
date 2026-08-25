package com.sc.qisi_system.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetDTO {

    /**
     * 用户名: 学生-学号 教师-工号
     */
    @NotBlank
    private String username;


    /**
     * 密码
     */
    @NotBlank
    private String password;


    /**
     * 验证码唯一标识码
     */
    @NotBlank
    private String captchaKey;


    /**
     * 验证码
     */
    @NotBlank
    private String captchaCode;
}
