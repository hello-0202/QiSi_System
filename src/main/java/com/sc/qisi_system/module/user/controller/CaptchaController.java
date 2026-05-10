package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.service.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 验证码服务控制器
 */
@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class CaptchaController {


    private final CaptchaService captchaService;


    /**
     * 获取图片验证码接口
     *
     * @param request 无请求参数
     * @return 成功返回验证码相关信息，失败返回错误信息
     */
    @GetMapping("/captcha")
    public Result getCaptcha(HttpServletRequest request) {
        return captchaService.getCaptcha(request);
    }
}
