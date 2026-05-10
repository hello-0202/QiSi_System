package com.sc.qisi_system.module.minio.service;

import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import com.sc.qisi_system.module.demand.vo.AttachmentListVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


/**
 * Minio 文件存储业务服务接口
 * 提供附件上传/删除/下载、头像上传、文件URL生成等功能
 */
public interface MinioService {


    /**
     * 批量上传需求附件
     *
     * @param demandId 需求ID
     * @param files 附件文件数组
     * @return 上传结果VO
     * @throws Exception 上传异常
     */
    AttachmentUploadVO batchUploadDemandAttachment(Long demandId, MultipartFile[] files) throws Exception;


    /**
     * 获取需求附件列表
     *
     * @param demandId 需求ID
     * @return 附件列表
     */
    List<AttachmentListVO> getDemandAttachmentList(Long demandId);


    /**
     * 删除单个需求附件
     *
     * @param attachmentId 附件ID
     */
    void deleteAttachment(Long attachmentId);


    /**
     * 批量删除需求附件
     *
     * @param attachmentIds 附件ID集合
     */
    void deleteBatchAttachment(List<Long> attachmentIds);


    /**
     * 批量上传实践进度附件
     *
     * @param demandId 需求ID
     * @param files 附件文件数组
     * @return 上传结果VO
     * @throws Exception 上传异常
     */
    AttachmentUploadVO batchUploadProgressAttachments(Long demandId, MultipartFile[] files) throws Exception;


    /**
     * 获取实践进度附件列表
     *
     * @param demandId 需求ID
     * @return 附件列表
     */
    List<AttachmentListVO> getProgressAttachmentList(Long demandId);


    /**
     * 删除单个实践进度附件
     *
     * @param attachmentId 附件ID
     */
    void deleteProgressAttachment(Long attachmentId);


    /**
     * 批量删除实践进度附件
     *
     * @param attachmentIds 附件ID集合
     */
    void deleteBatchProgressAttachment(List<Long> attachmentIds);


    /**
     * 下载文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件对象名称
     * @return 文件输入流
     */
    InputStream downloadFile(String bucketName, String objectName);


    /**
     * 生成文件临时访问URL
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件对象名称
     * @return 临时访问URL
     */
    String generateUrl(String bucketName, String objectName);


    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param file 头像文件
     * @return 头像存储路径
     */
    String updateUserAvatar(Long userId, MultipartFile file);


    /**
     * 获取用户头像访问URL（根据 avatar 字段）
     *
     * @param avatarPath 数据库中存储的头像路径
     * @return 可直接访问的http完整URL
     */
    String getUserAvatarUrl(String avatarPath);
}