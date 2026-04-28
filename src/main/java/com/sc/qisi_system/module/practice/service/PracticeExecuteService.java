package com.sc.qisi_system.module.practice.service;


import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.DemandProgressDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PracticeExecuteService {


    /**
     * 提交需求日志
     *
     * @param demandProgressDTO 需求日志请求体
     */
    void submitDemandLog(DemandProgressDTO demandProgressDTO);


    AttachmentUploadVO batchUploadProgressAttachments(Long demandId, MultipartFile[] files) throws Exception;


    /**
     * 更新需求计划
     *
     * @param demandPlanDTO 需求计划请求体
     */
    void updateDemandPlan(DemandPlanDTO demandPlanDTO);


    void deleteProgressAttachment(Long attachmentId);


    void deleteBatchProgressAttachment(List<Long> attachmentIds);


    /**
     * 主动申请退出需求接
     *
     * @param memberChangeDTO 需求成员变更请求体
     */
    void applyQuitDemand(MemberChangeDTO memberChangeDTO);
}
