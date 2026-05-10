package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;
import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 需求发布业务服务接口
 * 提供需求草稿管理、审核提交、附件上传与删除等发布相关操作
 */
public interface DemandPublishService {


    /**
     * 提交需求草稿
     * 角色: 发布者
     *
     * @param userId                  用户ID
     * @param demandPublishDraftDTO 请求参数
     * @return 草稿ID
     */
    Long submitDraft(Long userId, DemandPublishDraftDTO demandPublishDraftDTO);


    /**
     * 修改需求草稿
     * 角色: 发布者
     *
     * @param demandUpdateDraftDTO 请求参数
     * @return 草稿ID
     */
    Long updateDraft(DemandUpdateDraftDTO demandUpdateDraftDTO);


    /**
     * 提交需求审核
     * 角色: 发布者
     *
     * @param demandId 需求ID
     * @return 需求ID
     */
    Long submitAudit(Long demandId);


    /**
     * 撤销需求审核
     * 角色: 发布者
     *
     * @param userId   用户ID
     * @param demandId 需求ID
     * @return 需求ID
     */
    Long cancelSubmit(Long userId, Long demandId);


    /**
     * 批量上传需求附件
     * 角色: 发布者
     *
     * @param demandId 需求ID
     * @param files    附件文件数组
     * @return 上传结果VO
     * @throws Exception 上传异常
     */
    AttachmentUploadVO batchUploadDemandAttachment(Long demandId, MultipartFile[] files) throws Exception;


    /**
     * 删除单个需求附件
     * 角色: 发布者
     *
     * @param attachmentId 附件ID
     */
    void deleteAttachment(Long attachmentId);


    /**
     * 批量删除需求附件
     * 角色: 发布者
     *
     * @param attachmentIds 附件ID列表
     */
    void deleteBatchAttachment(List<Long> attachmentIds);
}