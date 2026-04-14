package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.Result;
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
    Result batchUploadDemandAttachment(Long demandId, MultipartFile[] files) throws Exception;


    InputStream downloadFile(String bucketName, String objectName);

    Result getDemandAttachmentList(Long demandId);

    Result deleteAttachment(Long attachmentId);

    Result deleteBatchAttachment(List<Long> attachmentIds);

    String generateUrl(String bucketName, String objectName);


}
