package com.sc.qisi_system.module.user.service.impl;

import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.properties.CaptchaProperties;
import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.CaptchaUtils;
import com.sc.qisi_system.common.utils.RequestUtils;
import com.sc.qisi_system.module.user.service.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final StringRedisTemplate redisTemplate;
    private final CaptchaUtils captchaUtils;
    private final RequestUtils requestUtils;
    private final CaptchaProperties captchaProperties;

    /**
     * 生成验证码
     */
    @Override
    public Result getCaptcha(HttpServletRequest request) {

        String lockKey = "captcha:limit:" + requestUtils.getClientIp(request);
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(
                lockKey,
                "1",
                captchaProperties.getIpLockTtl(),
                TimeUnit.SECONDS
        );

        if (Boolean.FALSE.equals(acquired)) {
            throw new BusinessException(ResultCode.CAPTCHA_REQUEST_FREQUENTLY);
        }

        Map<String, String> map = captchaUtils.generateCaptcha();

        String captchaKey = "captcha:code:" + UUID.randomUUID();
        redisTemplate.opsForValue().set(
                captchaKey,
                map.get("code"),
                captchaProperties.getTtl(),
                TimeUnit.SECONDS
        );

        map.remove("code");
        map.put("captchaKey", captchaKey);

        return Result.success(map);
    }

    /**
     * 业务逻辑验证
     */
    public void checkCaptcha(String captchaKey, String captchaCode) {

        if (!StringUtils.hasText(captchaKey) || !StringUtils.hasText(captchaCode)) {
            throw new BusinessException(ResultCode.CAPTCHA_EMPTY);
        }

        if(isCaptchaInvalid(captchaKey, captchaCode)){
            throw new BusinessException(ResultCode.CAPTCHA_EXPIRED_OR_CAPTCHA_ERROR);
        }

    }

    /**
     * 校验验证码
     */
    private boolean isCaptchaInvalid(String captchaKey, String captchaCode) {

        String correctCode = redisTemplate.opsForValue().get(captchaKey);
        redisTemplate.delete(captchaKey);
        return correctCode == null || !correctCode.equalsIgnoreCase(captchaCode.trim());

    }

}
