package com.sc.qisi_system.module.minio.service;

import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import com.sc.qisi_system.module.demand.vo.AttachmentListVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface MinioService {

    /**
     * 需求上传附件
     * @param demandId 需求ID
     * @param files 上传的文件
     * @return 文件信息/访问路径
     */
    AttachmentUploadVO batchUploadDemandAttachment(Long demandId, MultipartFile[] files) throws Exception;


    List<AttachmentListVO> getDemandAttachmentList(Long demandId);


    void deleteAttachment(Long attachmentId);


    void deleteBatchAttachment(List<Long> attachmentIds);


    AttachmentUploadVO batchUploadProgressAttachments(Long demandId, MultipartFile[] files) throws Exception;


    List<AttachmentListVO> getProgressAttachmentList(Long demandId);


    void deleteProgressAttachment(Long attachmentId);


    void deleteBatchProgressAttachment(List<Long> attachmentIds);


    InputStream downloadFile(String bucketName, String objectName);


    String generateUrl(String bucketName, String objectName);


    String updateUserAvatar(Long userId, MultipartFile file);

    /**
     * 获取用户头像访问URL（根据 avatar 字段）
     * @param avatarPath 数据库中存储的头像路径
     * @return 可直接访问的http完整URL
     */
    String getUserAvatarUrl(String avatarPath);


}
