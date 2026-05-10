package com.sc.qisi_system.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {


    /**
     * 用户名: 学号/工号/手机号
     */
    @NotBlank(message = "用户名不能为空")
    private String username;


    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;


    /**
     * 验证码唯一标识码
     */
    private String captchaKey;


    /**
     * 验证码
     */
    private String captchaCode;
}
