package com.sc.qisi_system.config.minio;

import com.sc.qisi_system.common.exception.SystemException;
import com.sc.qisi_system.common.result.ResultCode;
import io.minio.MinioClient;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.connect-timeout}")
    private int connectTimeout;

    @Value("${minio.read-timeout}")
    private int readTimeout;

    @Value("${minio.write-timeout}")
    private int writeTimeout;

    @Getter
    @Value("${minio.bucket.avatar}")
    private String avatarBucket;

    @Getter
    @Value("${minio.bucket.attachment}")
    private String attachmentBucket;

    @Bean
    public MinioClient getMinioClient() {

        log.info("开始初始化MinIO客户端");
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
            builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

            OkHttpClient okHttpClient = builder.build();

            log.info("MinIO客户端初始化完成");

            return MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .httpClient(okHttpClient)
                    .build();
        } catch (Exception e) {
            log.error("Minio客户端初始化失败", e);
            throw new SystemException(ResultCode.SYSTEM_ERROR,"MinioK客户端初始化失败");
        }
    }

    @PostConstruct
    public void checkMinioConfig() {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR,"minio.endpoint 配置不能为空，请检查application.yml");
        }
        if (accessKey == null || accessKey.trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR,"minio.access-key 配置不能为空，请检查application.yml");
        }
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR,"minio.secret-key 配置不能为空，请检查application.yml");
        }
    }


}
