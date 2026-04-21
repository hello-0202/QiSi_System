package com.sc.qisi_system.module.demand.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.exception.SystemException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.config.minio.MinioConfig;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.entity.DemandAttachment;
import com.sc.qisi_system.module.demand.mapper.DemandAttachmentMapper;
import com.sc.qisi_system.module.demand.mapper.DemandMapper;
import com.sc.qisi_system.module.demand.service.AsyncFileDeleteService;
import com.sc.qisi_system.module.demand.service.DemandAttachmentService;
import com.sc.qisi_system.module.demand.service.MinioService;
import com.sc.qisi_system.module.demand.domain.DemandAttachmentFail;
import com.sc.qisi_system.module.demand.domain.DemandAttachmentSuccess;
import com.sc.qisi_system.module.demand.vo.DemandAttachmentUploadVO;
import com.sc.qisi_system.module.demand.vo.DemandAttachmentListVO;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class MinioServiceImpl implements MinioService {


    private final DemandMapper demandMapper;
    private final DemandAttachmentMapper demandAttachmentMapper;
    private final DemandAttachmentService demandAttachmentService;
    private final AsyncFileDeleteService asyncFileDeleteService;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public DemandAttachmentUploadVO batchUploadDemandAttachment(Long demandId, MultipartFile[] files) {

        LambdaQueryWrapper<Demand> queryWrapper = Wrappers.<Demand>lambdaQuery()
                .eq(Demand::getId, demandId);

        if(demandMapper.selectOne(queryWrapper) == null) {
            throw new BusinessException(ResultCode.DEMAND_NOT_EXIST);
        }

        if (files == null || files.length == 0) {

            DemandAttachmentUploadVO emptyVO = new DemandAttachmentUploadVO();
            emptyVO.setSuccessCount(0);
            emptyVO.setFailCount(0);
            emptyVO.setSuccessFiles(Collections.emptyList());
            emptyVO.setFailFiles(Collections.emptyList());

            return emptyVO;
        }

        // 初始化VO相关集合
        List<DemandAttachment> attachments = new ArrayList<>();
        List<DemandAttachmentSuccess> successFiles = new ArrayList<>();
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
                String objectName = "demand/" + demandId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

                // 上传到MinIO
                uploadFileToMinio(minioConfig.getAttachmentBucket(), file, objectName);

                // 生成文件访问链接
                String fileUrl = generateUrl(minioConfig.getAttachmentBucket(), objectName);

                String suffix = FileUtil.extName(file.getOriginalFilename());

                // 构建【成功文件VO】
                DemandAttachmentSuccess successVO = new DemandAttachmentSuccess();
                successVO.setOriginalFileName(file.getOriginalFilename());
                successVO.setFileType(suffix);
                successVO.setFileSize(file.getSize());
                successVO.setFileUrl(fileUrl);
                successVO.setDemandId(demandId);
                successFiles.add(successVO);

                // 构建附件实体
                DemandAttachment attachment = new DemandAttachment();
                attachment.setDemandId(demandId);
                attachment.setFileName(file.getOriginalFilename());
                attachment.setObjectName(objectName);
                attachment.setBucketName(minioConfig.getAttachmentBucket());
                attachment.setFileType(suffix);
                attachment.setFileSize(file.getSize());
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

        // 3. 批量插入数据库
        if (!attachments.isEmpty()) {
            try {

                boolean saveResult = demandAttachmentService.saveBatch(attachments);
                if (!saveResult) {
                    throw new Exception("批量插入数据库失败");
                }

                log.info("成功插入{}条需求附件记录", attachments.size());
            } catch (Exception e) {
                log.error("批量插入需求附件到数据库失败", e);
                rollbackMinioFiles(attachments);

                throw new SystemException(ResultCode.FILE_UPLOAD_DB_SAVE_FAILED);
            }
        }

        // 4. 封装最终返回VO
        DemandAttachmentUploadVO uploadVO = new DemandAttachmentUploadVO();
        uploadVO.setSuccessCount(successFiles.size());
        uploadVO.setFailCount(failFiles.size());
        uploadVO.setSuccessFiles(successFiles);
        uploadVO.setFailFiles(failFiles);

        // 5. 返回结构化结果
        return uploadVO;
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


    @Override
    public List<DemandAttachmentListVO> getDemandAttachmentList(Long demandId) {

        LambdaQueryWrapper<DemandAttachment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DemandAttachment::getDemandId, demandId)
                .eq(DemandAttachment::getIsDeleted, false);

        List<DemandAttachment> demandAttachments = demandAttachmentMapper.selectList(queryWrapper);
        List<DemandAttachmentListVO> demandAttachmentListVOS = new ArrayList<>();

        // 遍历附件，生成链接
        for (DemandAttachment attachment : demandAttachments) {
                DemandAttachmentListVO vo = new DemandAttachmentListVO();
                vo.setId(attachment.getId());
                vo.setFileName(attachment.getFileName());
                vo.setFileType(attachment.getFileType());
                vo.setFileSize(attachment.getFileSize());

                // 生成URL 可能抛异常 → 必须捕获
                String url = generateUrl(attachment.getBucketName(), attachment.getObjectName());
                vo.setUrl(url);

                demandAttachmentListVOS.add(vo);
        }
        return demandAttachmentListVOS;
    }


    @Override
    public void deleteAttachment(Long attachmentId) {

        DemandAttachment demandAttachment = demandAttachmentMapper.selectById(attachmentId);
        if(demandAttachment == null) {
            throw(new BusinessException(ResultCode.ATTACHMENT_NOT_FOUND));
        }

        demandAttachment.setIsDeleted(1);
        demandAttachmentMapper.updateById(demandAttachment);
        asyncFileDeleteService.deleteFileAsync(demandAttachment);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DemandAttachment> deleteBatchAttachment(List<Long> attachmentIds) {


        LambdaQueryWrapper<DemandAttachment> wrapper = Wrappers.lambdaQuery();
        wrapper.in(DemandAttachment::getId, attachmentIds)
                .eq(DemandAttachment::getIsDeleted, 0);

        List<DemandAttachment> existAttachments = demandAttachmentMapper.selectList(wrapper);

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

        return existAttachments;

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
     * 私有方法: 回滚MinIO已上传的文件
     */
    private void rollbackMinioFiles(List<DemandAttachment> attachments) {

        for (DemandAttachment attachment : attachments) {
            try {
                minioClient.removeObject(
                        io.minio.RemoveObjectArgs.builder()
                                .bucket(attachment.getBucketName())
                                .object(attachment.getObjectName())
                                .build()
                );
                log.info("回滚MinIO文件【{}】成功", attachment.getObjectName());
            } catch (Exception e) {
                log.error("回滚MinIO文件失败", e);
            }
        }
    }




}