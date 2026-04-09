package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;

public interface CaptchaService {

    Result getCaptcha(HttpServletRequest request);

    void checkCaptcha(String captchaKey, String captchaCode);
}
