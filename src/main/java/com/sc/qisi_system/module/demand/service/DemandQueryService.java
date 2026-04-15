package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.demand.dto.ApplicableDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.vo.ApplicableDemandVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandVO;

public interface DemandQueryService {


    /**
     * 查询草稿列表
     * @param userId 用户id
     * @return 返回草稿列表
     */
    PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize);


    /**
     * 条件查询需求列表
     * @param myDemandQueryDTO 查询请求体
     * @return 统一返回结果
     */
    PageResult<DemandListVO> getDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    /**
     * 查询可申请的需求列表
     *
     * @return 可申请需求列表
     */
    PageResult<DemandListVO> getApplicableList(ApplicableDemandQueryDTO applicableDemandQueryDTO);


    /**
     * 查询需求详情接口
     * @param demandId 需求id
     * @return 需求完整信息
     */
    DemandVO getDemandDetail(Long demandId);


    ApplicableDemandVO getApplicableDemandDetail(Long demandId);

}
