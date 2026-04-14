package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.demand.dto.DemandQueryDTO;

public interface DemandQueryService {


    /**
     * 查询草稿列表
     * @param userId 用户id
     * @return 返回草稿列表
     */
    Result getDraftList(Long userId, Integer pageNum, Integer pageSize);


    /**
     * 条件查询需求列表
     * @param demandQueryDTO 查询请求体
     * @return 统一返回结果
     */
    Result getDemandList(Long userId, DemandQueryDTO demandQueryDTO);


    /**
     * 查询需求详情接口
     * @param demandId 需求id
     * @return 需求完整信息
     */
    Result getDemandDetail(Long demandId);
}
