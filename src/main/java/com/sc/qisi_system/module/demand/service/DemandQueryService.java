package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.demand.dto.ApplicableDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.vo.ApplicableDemandVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.MyDemandVO;

public interface DemandQueryService {


    /**
     * 查询草稿列表
     * @param userId 用户id
     * @return 返回草稿列表
     */
    PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize);


    /**
     * 分页条件查询我的需求列表
     * @param myDemandQueryDTO 查询请求体
     * @return 统一返回结果
     */
    PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    /**
     * 查询需求详情接口
     * @param demandId 需求id
     * @return 需求完整信息
     */
    MyDemandVO getMyDemandDetail(Long demandId);


    /**
     * 查询可申请的需求列表
     *
     * @param userId 用户id
     * @param applicableDemandQueryDTO 查询请求体
     * @return 可申请需求列表
     */
    PageResult<DemandListVO> getApplicableList(Long userId,ApplicableDemandQueryDTO applicableDemandQueryDTO);


    /**
     * 查询可申请需求详情接口
     *
     * @param demandId 需求id
     * @return 需求详情列表
     */
    ApplicableDemandVO getApplicableDemandDetail(Long demandId);


    /**
     * 分页条件查询我申请的需求列表接口
     *
     * @param userId 用户id
     * @param myApplyQueryDTO 查询请求体
     * @return 我申请的需求列表
     */
    PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO);


}
