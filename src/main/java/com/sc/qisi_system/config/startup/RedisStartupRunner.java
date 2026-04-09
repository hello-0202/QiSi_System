package com.sc.qisi_system.config.startup;

import com.sc.qisi_system.common.exception.SystemException;
import com.sc.qisi_system.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Order(2)
@Slf4j
public class RedisStartupRunner implements ApplicationRunner {


    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            String pong = redisTemplate.execute(RedisConnectionCommands::ping);
            if (pong != null && pong.equals("PONG")) {
                log.info("Redis数据库连接成功");
            }
        } catch (Exception e) {
            throw new SystemException(ResultCode.SYSTEM_ERROR,"Redis数据库连接失败",e);
        }
    }
}
