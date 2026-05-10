package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 验证码业务服务接口
 * 提供验证码生成、验证码校验等核心功能
 */
public interface CaptchaService {


    /**
     * 获取图形验证码
     *
     * @param request 请求对象
     * @return 验证码信息
     */
    Result getCaptcha(HttpServletRequest request);


    /**
     * 校验验证码是否正确
     *
     * @param captchaKey 验证码键
     * @param captchaCode 用户输入的验证码
     */
    void checkCaptcha(String captchaKey, String captchaCode);
}