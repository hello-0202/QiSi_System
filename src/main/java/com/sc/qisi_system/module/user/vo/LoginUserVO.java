package com.sc.qisi_system.module.user.vo;

import lombok.Data;

/**
 * 登录后返回的用户信息VO
 */
@Data
public class LoginUserVO {


    /**
     * 访问令牌
     */
    private String accessToken;


    /**
     * 刷新令牌
     */
    private String refreshToken;


    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 用户名
     */
    private String username;


    /**
     * 用户类型编码
     */
    private Integer userType;


    /**
     * 用户类型描述
     */
    private String userTypeDesc;
}