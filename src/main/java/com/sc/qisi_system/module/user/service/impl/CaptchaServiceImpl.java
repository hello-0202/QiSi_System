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


/**
 * 验证码服务实现类
 */
@RequiredArgsConstructor
@Service
public class CaptchaServiceImpl implements CaptchaService {


    private final StringRedisTemplate redisTemplate;
    private final CaptchaUtils captchaUtils;
    private final RequestUtils requestUtils;
    private final CaptchaProperties captchaProperties;


    /**
     * 获取验证码（带IP限流）
     */
    @Override
    public Result getCaptcha(HttpServletRequest request) {
        // 1. 生成IP限流锁，防止频繁请求
        String lockKey = "captcha:limit:" + requestUtils.getClientIp(request);
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(
                lockKey,
                "1",
                captchaProperties.getIpLockTtl(),
                TimeUnit.SECONDS
        );

        // 2. 校验是否频繁请求
        if (Boolean.FALSE.equals(acquired)) {
            throw new BusinessException(ResultCode.CAPTCHA_REQUEST_FREQUENTLY);
        }

        // 3. 生成验证码
        Map<String, String> map = captchaUtils.generateCaptcha();

        // 4. 保存验证码到Redis
        String captchaKey = "captcha:code:" + UUID.randomUUID();
        redisTemplate.opsForValue().set(
                captchaKey,
                map.get("code"),
                captchaProperties.getTtl(),
                TimeUnit.SECONDS
        );

        // 5. 封装返回结果（移除明文code，携带验证码key）
        map.remove("code");
        map.put("captchaKey", captchaKey);

        return Result.success(map);
    }


    /**
     * 校验验证码（业务入口）
     */
    public void checkCaptcha(String captchaKey, String captchaCode) {
        // 1. 校验参数非空
        if (!StringUtils.hasText(captchaKey) || !StringUtils.hasText(captchaCode)) {
            throw new BusinessException(ResultCode.CAPTCHA_EMPTY);
        }

        // 2. 校验验证码正确性
        if(isCaptchaInvalid(captchaKey, captchaCode)){
            throw new BusinessException(ResultCode.CAPTCHA_EXPIRED_OR_CAPTCHA_ERROR);
        }
    }


    /**
     * 内部校验验证码是否无效（过期/错误）
     */
    private boolean isCaptchaInvalid(String captchaKey, String captchaCode) {
        // 1. 获取正确验证码
        String correctCode = redisTemplate.opsForValue().get(captchaKey);
        // 2. 校验后立即删除，防止重复使用
        redisTemplate.delete(captchaKey);
        // 3. 返回校验结果
        return correctCode == null || !correctCode.equalsIgnoreCase(captchaCode.trim());
    }

}