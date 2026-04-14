package com.sc.qisi_system.module.demand.vo;

import lombok.Data;

import java.util.List;

/**
 * 需求附件批量上传返回结果VO
 */
@Data
public class DemandAttachmentUploadVO {


    private Integer successCount;

    private Integer failCount;

    private List<DemandAttachmentSuccessVO> successFiles;

    private List<DemandAttachmentFailVO> failFiles;
}