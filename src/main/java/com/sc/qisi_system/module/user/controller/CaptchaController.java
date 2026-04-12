package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.service.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * 获取图片验证码
     */
    @GetMapping("/captcha")
    public Result getCaptcha(HttpServletRequest request) {
        return captchaService.getCaptcha(request);
    }


}
