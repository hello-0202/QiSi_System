package com.sc.qisi_system.module.demand.vo;

import com.sc.qisi_system.module.demand.domain.DemandAttachmentFail;
import com.sc.qisi_system.module.demand.domain.AttachmentSuccess;
import lombok.Data;

import java.util.List;

/**
 * 需求附件批量上传返回结果VO
 */
@Data
public class AttachmentUploadVO {


    private Integer successCount;

    private Integer failCount;

    private List<AttachmentSuccess> successFiles;

    private List<DemandAttachmentFail> failFiles;
}