package com.sc.qisi_system.module.minio.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.exception.SystemException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.config.minio.MinioConfig;
import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.demand.service.AsyncFileDeleteService;
import com.sc.qisi_system.module.demand.service.DemandAttachmentService;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.minio.service.MinioService;
import com.sc.qisi_system.module.demand.domain.DemandAttachmentFail;
import com.sc.qisi_system.module.demand.domain.AttachmentSuccess;
import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import com.sc.qisi_system.module.demand.vo.AttachmentListVO;
import com.sc.qisi_system.module.practice.entity.DemandProgressAttachment;
import com.sc.qisi_system.module.practice.service.DemandProgressAttachmentService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class MinioServiceImpl implements MinioService {


    private final DemandAttachmentService demandAttachmentService;
    private final DemandProgressAttachmentService demandProgressAttachmentService;
    private final AsyncFileDeleteService asyncFileDeleteService;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final DemandService demandService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AttachmentUploadVO batchUploadDemandAttachment(Long demandId, MultipartFile[] files) {

        if (demandService.notExistsByDemandId(demandId)) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        return commonBatchUpload(
                demandId,files,
                "demand/",
                (demandId1, originalFileName, objectName, bucketName, suffix, fileSize) -> {
                    DemandAttachment attachment = new DemandAttachment();
                    attachment.setDemandId(demandId1);
                    attachment.setFileName(originalFileName);
                    attachment.setObjectName(objectName);
                    attachment.setBucketName(bucketName);
                    attachment.setFileType(suffix);
                    attachment.setFileSize(fileSize);
                    return attachment;
                },
                demandAttachmentService::saveBatch,
                attachments -> commonRollbackMinioFiles(attachments,DemandAttachment::getBucketName, DemandAttachment::getObjectName)
        );
    }


    @Override
    public List<AttachmentListVO> getDemandAttachmentList(Long demandId) {

        LambdaQueryWrapper<DemandAttachment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandAttachment::getDemandId, demandId)
                .eq(DemandAttachment::getIsDeleted, false);

        List<DemandAttachment> demandAttachments = demandAttachmentService.list(queryWrapper);
        List<AttachmentListVO> voList = new ArrayList<>();

        for (DemandAttachment attachment : demandAttachments) {
            AttachmentListVO vo = buildAttachmentListVO(attachment);
            voList.add(vo);
        }
        return voList;
    }


    @Override
    public void deleteAttachment(Long attachmentId) {

        DemandAttachment demandAttachment = demandAttachmentService.getById(attachmentId);
        if (demandAttachment == null) {
            throw (new BusinessException(ResultCode.ATTACHMENT_NOT_FOUND));
        }

        demandAttachment.setIsDeleted(1);
        demandAttachmentService.updateById(demandAttachment);
        asyncFileDeleteService.deleteFileAsync(demandAttachment);

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchAttachment(List<Long> attachmentIds) {


        LambdaQueryWrapper<DemandAttachment> wrapper = Wrappers.lambdaQuery();
        wrapper.in(DemandAttachment::getId, attachmentIds)
                .eq(DemandAttachment::getIsDeleted, 0);

        List<DemandAttachment> existAttachments = demandAttachmentService.list(wrapper);

        if (existAttachments.isEmpty()) {
            throw new BusinessException(ResultCode.ATTACHMENT_NOT_FOUND);
        }

        // 2. 对存在的附件批量软删除
        for (DemandAttachment attachment : existAttachments) {
            attachment.setIsDeleted(1);
        }

        demandAttachmentService.updateBatchById(existAttachments);

        // 3. 异步删除文件
        for (DemandAttachment attachment : existAttachments) {
            asyncFileDeleteService.deleteFileAsync(attachment);
        }
    }


    @Override
    public AttachmentUploadVO batchUploadProgressAttachments(Long demandId, MultipartFile[] files) {
        if (demandService.notExistsByDemandId(demandId)) {
            throw (new SystemException(ResultCode.MINIO_DOWNLOAD_FAILED));
        }

        return commonBatchUpload(
                demandId,files,
                "progress/",
                (demandId1, originalFileName, objectName, bucketName, suffix, fileSize) -> {
                    DemandProgressAttachment attachment = new DemandProgressAttachment();
                    attachment.setDemandId(demandId1);
                    attachment.setFileName(originalFileName);
                    attachment.setObjectName(objectName);
                    attachment.setBucketName(bucketName);
                    attachment.setFileType(suffix);
                    attachment.setFileSize(fileSize);
                    return attachment;
                },
                demandProgressAttachmentService::saveBatch,
                attachments -> commonRollbackMinioFiles(attachments,DemandProgressAttachment::getBucketName, DemandProgressAttachment::getObjectName)
        );
    }


    @Override
    public List<AttachmentListVO> getProgressAttachmentList(Long demandId) {
        LambdaQueryWrapper<DemandProgressAttachment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandProgressAttachment::getDemandId, demandId)
                .eq(DemandProgressAttachment::getIsDeleted, false);

        List<DemandProgressAttachment> progressAttachments = demandProgressAttachmentService.list(queryWrapper);
        List<AttachmentListVO> voList = new ArrayList<>();

        for (DemandProgressAttachment attachment : progressAttachments) {
            AttachmentListVO vo = buildAttachmentListVO(attachment); // 一行搞定
            voList.add(vo);
        }
        return voList;
    }


    /**
     * 删除单个进度附件
     */
    @Override
    public void deleteProgressAttachment(Long attachmentId) {
        DemandProgressAttachment attachment = demandProgressAttachmentService.getById(attachmentId);
        if (attachment == null) {
            throw new BusinessException(ResultCode.ATTACHMENT_NOT_FOUND);
        }

        attachment.setIsDeleted(1);
        demandProgressAttachmentService.updateById(attachment);
        asyncFileDeleteService.deleteProgressFileAsync(attachment);
    }


    /**
     * 批量删除进度附件
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchProgressAttachment(List<Long> attachmentIds) {
        LambdaQueryWrapper<DemandProgressAttachment> wrapper = Wrappers.lambdaQuery();
        wrapper.in(DemandProgressAttachment::getId, attachmentIds)
                .eq(DemandProgressAttachment::getIsDeleted, 0);

        List<DemandProgressAttachment> existAttachments = demandProgressAttachmentService.list(wrapper);

        if (existAttachments.isEmpty()) {
            throw new BusinessException(ResultCode.ATTACHMENT_NOT_FOUND);
        }

        // 软删除
        for (DemandProgressAttachment attachment : existAttachments) {
            attachment.setIsDeleted(1);
        }
        demandProgressAttachmentService.updateBatchById(existAttachments);

        // 异步删除文件
        for (DemandProgressAttachment attachment : existAttachments) {
            asyncFileDeleteService.deleteProgressFileAsync(attachment);
        }
    }


    /**
     * 下载文件
     */
    @Override
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("MinIO文件【{}】下载失败（桶：{}）", objectName, bucketName, e);
            throw new SystemException(ResultCode.MINIO_DOWNLOAD_FAILED, "文件下载失败：" + e.getMessage());
        }
    }



    /**
     * 获取文件临时访问链接
     */
    @Override
    public String generateUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取MinIO文件【{}】访问链接失败（桶：{}）", objectName, bucketName, e);
            return null;
        }
    }


    // 泛型方法：自动识别是 DemandAttachment 还是 DemandProgressAttachment
    private <T> AttachmentListVO buildAttachmentListVO(T attachment) {
        AttachmentListVO vo = new AttachmentListVO();

        Long id = null;
        String fileName = null;
        String fileType = null;
        Long fileSize = null;
        String bucketName = null;
        String objectName = null;

        // 自动判断类型
        if (attachment instanceof DemandAttachment a) {
            id = a.getId();
            fileName = a.getFileName();
            fileType = a.getFileType();
            fileSize = a.getFileSize();
            bucketName = a.getBucketName();
            objectName = a.getObjectName();
        }
        else if (attachment instanceof DemandProgressAttachment a) {
            id = a.getId();
            fileName = a.getFileName();
            fileType = a.getFileType();
            fileSize = a.getFileSize();
            bucketName = a.getBucketName();
            objectName = a.getObjectName();
        }

        vo.setId(id);
        vo.setFileName(fileName);
        vo.setFileType(fileType);
        vo.setFileSize(fileSize);

        try {
            String url = generateUrl(bucketName, objectName);
            vo.setUrl(url);
        } catch (Exception e) {
            log.error("附件URL生成失败 id:{}", id, e);
            vo.setUrl(null);
        }

        return vo;
    }


    /**
     * 私有方法: 底层上传文件到MinIO
     */
    private void uploadFileToMinio(String bucketName, MultipartFile file, String objectName) {
        try {
            // 检查桶是否存在，不存在则创建
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("MinIO桶【{}】创建成功", bucketName);
            }

            // 上传文件流
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }
            log.info("文件【{}】上传到MinIO桶【{}】成功", objectName, bucketName);

        } catch (Exception e) {
            log.error("文件【{}】上传到MinIO桶【{}】失败", objectName, bucketName, e);
            throw new SystemException(ResultCode.MINIO_DOWNLOAD_FAILED);
        }
    }


    /**
     * 通用 MinIO 文件回滚逻辑
     */
    private <T> void commonRollbackMinioFiles(List<T> attachments, Function<T, String> bucketExtractor, Function<T, String> objectExtractor) {
        for (T attachment : attachments) {
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketExtractor.apply(attachment))
                                .object(objectExtractor.apply(attachment))
                                .build()
                );
                log.info("回滚MinIO文件【{}】成功", objectExtractor.apply(attachment));
            } catch (Exception e) {
                log.error("回滚MinIO文件失败", e);
            }
        }
    }


    private <T> AttachmentUploadVO commonBatchUpload(
        Long demandId,
        MultipartFile[] files,
        String pathPrefix,
        EntityBuilder<T> entityBuilder,
        BatchSaver<T> batchSaver,
        RollBacker<T> rollBacker
    ){
        if (isEmpty(files)) {
            return buildEmptyUploadVO();
        }

        // 初始化VO相关集合
        List<T> attachments = new ArrayList<>();
        List<AttachmentSuccess> successFiles = new ArrayList<>();
        List<DemandAttachmentFail> failFiles = new ArrayList<>();

        // 2. 循环处理每个文件（异常局部捕获，不中断整批处理）
        for (MultipartFile file : files) {
            try {
                // 跳过空文件
                if (file.isEmpty()) {
                    log.warn("跳过空文件：{}", file.getOriginalFilename());

                    DemandAttachmentFail failVO = new DemandAttachmentFail();
                    failVO.setOriginalFileName(file.getOriginalFilename());
                    failVO.setFailReason("文件为空，跳过上传");
                    failFiles.add(failVO);

                    continue;
                }

                // 构造MinIO唯一文件名
                String objectName = pathPrefix + demandId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

                // 上传到MinIO
                uploadFileToMinio(minioConfig.getAttachmentBucket(), file, objectName);

                // 生成文件访问链接
                String fileUrl = generateUrl(minioConfig.getAttachmentBucket(), objectName);
                String suffix = FileUtil.extName(file.getOriginalFilename());

                successFiles.add(buildSuccessVO(file.getOriginalFilename(), suffix, file.getSize(), fileUrl, demandId));

                T attachment = entityBuilder.build(demandId, file.getOriginalFilename(), objectName, minioConfig.getAttachmentBucket(), suffix, file.getSize());
                attachments.add(attachment);

            } catch (Exception e) {
                // 捕获单个文件的异常，记录并继续处理下一个文件
                String originalFileName = file.getOriginalFilename();
                log.error("文件【{}】上传失败", originalFileName, e);

                DemandAttachmentFail failVO = new DemandAttachmentFail();
                failVO.setOriginalFileName(originalFileName);
                failVO.setFailReason(e.getMessage());
                failFiles.add(failVO);
            }
        }
        // 3. 批量入库
        saveAttachmentsToDb(attachments, batchSaver, rollBacker);

        // 4. 封装返回
        return buildUploadVO(successFiles, failFiles);
    }


    private <T> void saveAttachmentsToDb(List<T> attachments, BatchSaver<T> batchSaver, RollBacker<T> rollbacker) {
        if (!attachments.isEmpty()) {
            try {
                boolean saveResult = batchSaver.saveBatch(attachments);
                if (!saveResult) {
                    throw new Exception("批量插入数据库失败");
                }
                log.info("成功插入{}条附件记录", attachments.size());
            } catch (Exception e) {
                log.error("批量插入附件到数据库失败", e);
                rollbacker.rollback(attachments);
                throw new SystemException(ResultCode.FILE_UPLOAD_DB_SAVE_FAILED);
            }
        }
    }


    private AttachmentSuccess buildSuccessVO(String originalFileName, String suffix, Long fileSize, String fileUrl, Long demandId) {
        AttachmentSuccess successVO = new AttachmentSuccess();
        successVO.setOriginalFileName(originalFileName);
        successVO.setFileType(suffix);
        successVO.setFileSize(fileSize);
        successVO.setFileUrl(fileUrl);
        successVO.setDemandId(demandId);
        return successVO;
    }


    private boolean isEmpty(MultipartFile[] files) {
        return files == null || files.length == 0;
    }


    private AttachmentUploadVO buildEmptyUploadVO(){
        AttachmentUploadVO emptyVO = new AttachmentUploadVO();
        emptyVO.setSuccessCount(0);
        emptyVO.setFailCount(0);
        emptyVO.setSuccessFiles(Collections.emptyList());
        emptyVO.setFailFiles(Collections.emptyList());
        return emptyVO;
    }


    private AttachmentUploadVO buildUploadVO(List<AttachmentSuccess> successFiles, List<DemandAttachmentFail> failFiles) {
        AttachmentUploadVO uploadVO = new AttachmentUploadVO();
        uploadVO.setSuccessCount(successFiles.size());
        uploadVO.setFailCount(failFiles.size());
        uploadVO.setSuccessFiles(successFiles);
        uploadVO.setFailFiles(failFiles);
        return uploadVO;
    }


    @FunctionalInterface
    private interface EntityBuilder<T> {
        T build(Long demandId, String originalFileName, String objectName, String bucketName, String suffix, Long fileSize);
    }


    @FunctionalInterface
    private interface BatchSaver<T>{
        boolean saveBatch(List<T> list);
    }


    @FunctionalInterface
    private interface RollBacker<T>{
        void rollback(List<T> list);
    }


}