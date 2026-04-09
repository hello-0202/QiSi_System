package com.sc.qisi_system.config.startup;

import com.sc.qisi_system.common.exception.SystemException;
import com.sc.qisi_system.common.result.ResultCode;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Order(1)
@Slf4j
public class MinioStartupRunner implements ApplicationRunner {


    private final MinioClient minioClient;

    @Override
    public void run(ApplicationArguments args) {
        log.info("开始健康检查");
        try {
            minioClient.listBuckets();
            log.info("Minio连接成功");
        } catch (Exception e) {
            throw new SystemException(ResultCode.SYSTEM_ERROR,"Minio连接失败",e);
        }


    }
}
