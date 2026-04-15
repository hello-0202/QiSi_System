package com.sc.qisi_system.module.user.dto;

import lombok.Data;

@Data
public class EnterpriseRegisterRequest {


    /**
     * 用户名: 手机号
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
     * 邮箱
     */
    private String email;


    /**
     * 用户类型: 3-企业人员
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
