package com.sc.qisi_system.module.demand.domain;

import lombok.Data;

/**
 * 上传失败的附件详情VO
 */
@Data
public class DemandAttachmentFail {

    private String originalFileName;

    private String failReason;
}