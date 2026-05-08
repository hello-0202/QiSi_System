package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.MyDemandDetailVO;


public interface DemandQueryService {


    /**
     * 查询草稿列表
     *
     * @param userId 用户id
     * @return 返回草稿列表
     */
    PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize);


    /**
     * 分页条件查询我的需求列表
     *
     * @param myDemandQueryDTO 查询请求体
     * @return 统一返回结果
     */
    PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    /**
     * 查询需求详情
     *
     * @param demandId 需求id
     * @return 需求完整信息
     */
    MyDemandDetailVO getMyDemandDetail(Long demandId);


    DemandPublicDetailVO getPublicDemandDetail(Long demandId);



}
