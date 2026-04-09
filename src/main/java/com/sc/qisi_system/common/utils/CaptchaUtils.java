package com.sc.qisi_system.common.utils;

import cn.hutool.captcha.CircleCaptcha;
import com.sc.qisi_system.common.properties.CaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class CaptchaUtils {

    private final CaptchaProperties captchaProperties;


    public Map<String, String> generateCaptcha() {

        CircleCaptcha captcha = new CircleCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight(), captchaProperties.getLength());
        Map<String, String> map = new HashMap<>();

        String code = captcha.getCode();
        String imageBase64 = captcha.getImageBase64Data();

        map.put("code", code);
        map.put("imageBase64", imageBase64);

        return map;
    }

}