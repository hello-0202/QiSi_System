package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;
import com.sc.qisi_system.module.demand.vo.AttachmentUploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DemandPublishService {

    /**
     * 提交需求草稿
     * @param demandPublishDraftDTO 请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    Long submitDraft(Long userId,DemandPublishDraftDTO demandPublishDraftDTO);


    /**
     * 修改需求草稿
     * @param demandUpdateDraftDTO 请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    Long updateDraft(DemandUpdateDraftDTO demandUpdateDraftDTO);


    /**
     * 提交审核
     * @param demandId 需求id
     * @return 返回统一结果
     */
    Long submitAudit(Long demandId);


    /**
     * 撤销审核
     *
     * @param userId 用户id
     * @param demandId 撤销需求id
     * @return 统一返回结果
     */
    Long cancelSubmit(Long userId,Long demandId);


    /**
     * 需求附件上传
     *
     * @param demandId 需求ID
     * @param files    上传的文件
     * @return 文件信息/访问路径
     */
    AttachmentUploadVO batchUploadDemandAttachment(Long demandId, MultipartFile[] files) throws Exception;


    /**
     * 删除需求附件
     *
     * @param attachmentId 附件ID
     */
    void deleteAttachment(Long attachmentId);


    /**
     * 批量删除需求附件
     *
     * @param attachmentIds 需求附件id列表
     */
    void deleteBatchAttachment(List<Long> attachmentIds);


}
