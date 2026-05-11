package com.sc.qisi_system.module.demand.service.impl;

import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.demand.service.AsyncFileDeleteService;
import com.sc.qisi_system.module.demand.service.DemandAttachmentService;
import com.sc.qisi_system.module.practice.entity.DemandProgressAttachment;
import com.sc.qisi_system.module.practice.service.DemandProgressAttachmentService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * 异步文件删除服务实现类
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class AsyncFileDeleteServiceImpl implements AsyncFileDeleteService {


    private final MinioClient minioClient;
    private final DemandAttachmentService demandAttachmentService;
    private final DemandProgressAttachmentService demandProgressAttachmentService;


    /**
     * 异步删除需求附件
     */
    @Async("asyncExecutor")
    @Override
    public void deleteFileAsync(DemandAttachment attachment) {
        try {
            // 1. 从MinIO中删除文件
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(attachment.getBucketName())
                            .object(attachment.getObjectName())
                            .build()
            );

            // 2. 删除数据库中的附件记录
            demandAttachmentService.removeById(attachment.getId());
            log.info("异步删除文件成功：{}", attachment.getFileName());

        } catch (Exception e) {
            log.error("异步删除文件失败：{}", attachment.getFileName(), e);
        }
    }


    /**
     * 异步删除进度附件
     */
    @Async("asyncExecutor")
    @Override
    public void deleteProgressFileAsync(DemandProgressAttachment attachment) {
        try {
            // 1. 从MinIO中删除文件
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(attachment.getBucketName())
                            .object(attachment.getObjectName())
                            .build()
            );

            // 2. 删除数据库中的进度附件记录
            demandProgressAttachmentService.removeById(attachment.getId());
            log.info("异步删除进度附件成功：{}", attachment.getFileName());

        } catch (Exception e) {
            log.error("异步删除进度附件失败：{}", attachment.getFileName(), e);
        }
    }
}