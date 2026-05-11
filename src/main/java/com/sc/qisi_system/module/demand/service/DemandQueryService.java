package com.sc.qisi_system.module.demand.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.MyDemandDetailVO;


/**
 * 需求查询服务接口
 * 功能: 草稿列表查询、我的需求列表查询、需求详情查询、公开需求详情查询
 */
public interface DemandQueryService {


    /**
     * 查询我的草稿列表
     * 角色: 发布者
     *
     * @param userId    用户ID
     * @param pageNum   页码
     * @param pageSize  每页条数
     * @return 草稿分页列表
     */
    PageResult<DemandListVO> getDraftList(Long userId, Integer pageNum, Integer pageSize);


    /**
     * 分页条件查询我的需求列表
     * 角色: 发布者
     *
     * @param userId            用户ID
     * @param myDemandQueryDTO  需求查询条件
     * @return 我的需求分页列表
     */
    PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    /**
     * 查询我的需求详情
     * 角色: 发布者
     *
     * @param demandId 需求ID
     * @return 我的需求详细信息
     */
    MyDemandDetailVO getMyDemandDetail(Long demandId);


    /**
     * 查询公开可申请需求详情
     * 角色: 发布者 认领者
     *
     * @param demandId 需求ID
     * @return 公开需求详细信息
     */
    DemandPublicDetailVO getPublicDemandDetail(Long demandId);
}