package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;

public interface DemandPublishService {

    /**
     * 提交需求草稿
     * @param demandPublishDraftDTO 请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    Result submitDraft(DemandPublishDraftDTO demandPublishDraftDTO);


    /**
     * 修改需求草稿
     * @param demandUpdateDraftDTO 请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    Result updateDraft(DemandUpdateDraftDTO demandUpdateDraftDTO);


    /**
     * 提交审核
     * @param demandId 需求id
     * @return 返回统一结果
     */
    Result submitAudit(Long demandId);



}
