package com.sc.qisi_system.module.demand.vo;

import lombok.Data;

/**
 * 成功上传的附件详情VO
 */
@Data
public class DemandAttachmentSuccessVO {

    private String originalFileName;

    private String fileType;

    private Long fileSize;

    private String fileUrl;

    private Long demandId;
}