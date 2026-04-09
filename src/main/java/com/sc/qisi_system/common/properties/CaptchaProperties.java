package com.sc.qisi_system.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    private int width;

    private int height;

    private int length;

    private long ttl;

    private long ipLockTtl;
}