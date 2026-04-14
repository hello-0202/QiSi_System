package com.sc.qisi_system.module.demand.vo;

import lombok.Data;

/**
 * 上传失败的附件详情VO
 */
@Data
public class DemandAttachmentFailVO {

    private String originalFileName;

    private String failReason;
}