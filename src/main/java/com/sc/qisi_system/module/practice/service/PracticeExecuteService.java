package com.sc.qisi_system.module.practice.service;

import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.DemandProgressDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 实践需求执行业务服务接口
 * 提供日志提交、附件上传、计划更新、退出申请等执行阶段功能
 */
public interface PracticeExecuteService {


    /**
     * 提交需求日志
     * 角色: 认领者
     *
     * @param demandProgressDTO 需求日志请求体
     */
    void submitDemandLog(DemandProgressDTO demandProgressDTO);


    /**
     * 批量上传实践进度附件
     * 角色: 认领者
     *
     * @param demandId 需求ID
     * @param files 附件文件数组
     * @return 上传结果VO
     * @throws Exception 上传异常
     */
    AttachmentUploadVO batchUploadProgressAttachments(Long demandId, MultipartFile[] files) throws Exception;


    /**
     * 更新需求计划
     * 角色: 认领者
     *
     * @param demandPlanDTO 需求计划请求体
     */
    void updateDemandPlan(DemandPlanDTO demandPlanDTO);


    /**
     * 删除实践进度单个附件
     * 角色: 认领者
     *
     * @param attachmentId 附件ID
     */
    void deleteProgressAttachment(Long attachmentId);


    /**
     * 批量删除实践进度附件
     * 角色: 认领者
     *
     * @param attachmentIds 附件ID集合
     */
    void deleteBatchProgressAttachment(List<Long> attachmentIds);


    /**
     * 主动申请退出需求
     * 角色: 认领者
     *
     * @param memberChangeDTO 需求成员变更请求体
     */
    void applyQuitDemand(MemberChangeDTO memberChangeDTO);
}