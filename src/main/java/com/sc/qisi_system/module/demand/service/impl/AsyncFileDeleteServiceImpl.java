package com.sc.qisi_system.module.demand.service.impl;

import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.demand.mapper.DemandAttachmentMapper;
import com.sc.qisi_system.module.demand.service.AsyncFileDeleteService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AsyncFileDeleteServiceImpl implements AsyncFileDeleteService {

    private final MinioClient minioClient;
    private final DemandAttachmentMapper demandAttachmentMapper;

    /**
     * 私有方法: 在Minio里删除文件
     */
    @Async("asyncExecutor")
    public void deleteFileAsync(DemandAttachment attachment) {

        try {
            // 1. 删除 MinIO 文件
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(attachment.getBucketName())
                            .object(attachment.getObjectName())
                            .build()
            );

            // 2. 文件删成功 → 物理删除库记录
            demandAttachmentMapper.deleteById(attachment.getId());
            log.info("异步删除文件成功：{}", attachment.getFileName());

        } catch (Exception e) {
            log.error("异步删除文件失败：{}", attachment.getFileName(), e);
        }
    }
}
