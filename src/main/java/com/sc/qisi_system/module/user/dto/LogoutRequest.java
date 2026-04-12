package com.sc.qisi_system.module.user.dto;

import lombok.Data;

@Data
public class LogoutRequest {

    /**
     * 主键ID
     */
    private String userId;

    /**
     * accessToken
     */
    private String accessToken;

    /**
     * refreshToken
     */
    private String refreshToken;
}
