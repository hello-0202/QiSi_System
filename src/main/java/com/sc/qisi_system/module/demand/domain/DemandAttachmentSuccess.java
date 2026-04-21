package com.sc.qisi_system.module.demand.domain;

import lombok.Data;

/**
 * 成功上传的附件详情VO
 */
@Data
public class DemandAttachmentSuccess {

    private String originalFileName;

    private String fileType;

    private Long fileSize;

    private String fileUrl;

    private Long demandId;
}