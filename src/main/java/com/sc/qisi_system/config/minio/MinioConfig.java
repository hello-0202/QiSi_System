package com.sc.qisi_system.config.minio;

import com.sc.qisi_system.config.minio.MinioProperties;
import com.sc.qisi_system.common.exception.SystemException;
import com.sc.qisi_system.common.result.ResultCode;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
@Configuration
@Slf4j
public class MinioConfig {

    private final MinioProperties minioProperties;

    private String avatarBucket;

    private String attachmentBucket;


    // 初始化桶名字段
    @PostConstruct
    public void initBucketNames() {
        this.avatarBucket = minioProperties.getAvatarBucket();
        this.attachmentBucket = minioProperties.getAttachmentBucket();
    }

    // 初始化MinioClient（保留原有超时配置、异常处理）
    @Bean
    public MinioClient getMinioClient() {
        log.info("开始初始化MinIO客户端");
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // 从Properties中获取超时配置
            builder.connectTimeout(minioProperties.getConnectTimeout(), TimeUnit.MILLISECONDS);
            builder.readTimeout(minioProperties.getReadTimeout(), TimeUnit.MILLISECONDS);
            builder.writeTimeout(minioProperties.getWriteTimeout(), TimeUnit.MILLISECONDS);

            OkHttpClient okHttpClient = builder.build();
            log.info("MinIO客户端初始化完成");

            return MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .httpClient(okHttpClient)
                    .build();
        } catch (Exception e) {
            log.error("Minio客户端初始化失败", e);
            throw new SystemException(ResultCode.SYSTEM_ERROR, "Minio客户端初始化失败");
        }
    }


    @PostConstruct
    public void checkMinioConfig() {

        if (minioProperties.getEndpoint() == null || minioProperties.getEndpoint().trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR, "minio.endpoint 配置不能为空，请检查application.yml");
        }
        if (minioProperties.getAccessKey() == null || minioProperties.getAccessKey().trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR, "minio.access-key 配置不能为空，请检查application.yml");
        }
        if (minioProperties.getSecretKey() == null || minioProperties.getSecretKey().trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR, "minio.secret-key 配置不能为空，请检查application.yml");
        }

        // Bucket配置校验
        if (minioProperties.getBucket() == null) {
            throw new SystemException(ResultCode.SYSTEM_ERROR, "minio.bucket 配置不能为空，请检查application.yml");
        }
        if (minioProperties.getAvatarBucket() == null || minioProperties.getAvatarBucket().trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR, "minio.bucket.avatar 配置不能为空，请检查application.yml");
        }
        if (minioProperties.getAttachmentBucket() == null || minioProperties.getAttachmentBucket().trim().isEmpty()) {
            throw new SystemException(ResultCode.SYSTEM_ERROR, "minio.bucket.attachment 配置不能为空，请检查application.yml");
        }
    }
}