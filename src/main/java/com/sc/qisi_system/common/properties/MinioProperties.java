package com.sc.qisi_system.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MinIO配置属性映射类（仅负责绑定yml配置，无业务逻辑）
 */
@Data
@Component // 注入Spring容器，方便Config类注入
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    // 基础连接配置
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private int connectTimeout;
    private int readTimeout;
    private int writeTimeout;

    // Bucket子配置（匹配yml的minio.bucket节点）
    private Bucket bucket;

    // 内部类：Bucket配置映射
    @Data
    public static class Bucket {
        private String avatar;
        private String attachment;
    }

    // 快捷获取桶名（简化Config类调用）
    public String getAvatarBucket() {
        return bucket.getAvatar();
    }

    public String getAttachmentBucket() {
        return bucket.getAttachment();
    }
}