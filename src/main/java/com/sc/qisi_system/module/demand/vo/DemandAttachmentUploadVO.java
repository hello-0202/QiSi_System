package com.sc.qisi_system.module.demand.vo;

import com.sc.qisi_system.module.demand.domain.DemandAttachmentFail;
import com.sc.qisi_system.module.demand.domain.DemandAttachmentSuccess;
import lombok.Data;

import java.util.List;

/**
 * 需求附件批量上传返回结果VO
 */
@Data
public class DemandAttachmentUploadVO {


    private Integer successCount;

    private Integer failCount;

    private List<DemandAttachmentSuccess> successFiles;

    private List<DemandAttachmentFail> failFiles;
}