package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;

public interface PublishService {

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


    /**
     * 查询草稿列表
     * @param userId 用户id
     * @return 返回草稿列表
     */
    Result getDraftList(Long userId, Integer pageNum, Integer pageSize);


    /**
     * 查询需求详情接口
     * @param demandId 需求id
     * @return 需求完整信息
     */
    Result getDemandDetail(Long demandId);
}
