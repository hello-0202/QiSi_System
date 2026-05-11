package com.sc.qisi_system.module.demand.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;

import java.util.Map;


/**
 * 需求核心服务接口
 * 功能: 需求基础管理、我的需求/申请/参与列表查询、需求详情查询、分页结果转换等核心业务逻辑
 */
public interface DemandService extends IService<Demand> {


    /**
     * 判断需求是否不存在
     *
     * @param demandId 需求ID
     * @return 不存在返回true，存在返回false
     */
    boolean isNotExistsByDemandId(Long demandId);


    /**
     * 查询我的申请需求列表
     *
     * @param userId 用户ID
     * @param myApplyQueryDTO 查询条件
     * @return 我的申请需求分页列表
     */
    PageResult<DemandListVO> getMyApplyDemandList(Long userId, MyApplyQueryDTO myApplyQueryDTO);


    /**
     * 查询我的发布需求列表
     *
     * @param userId 用户ID
     * @param myDemandQueryDTO 查询条件
     * @return 我的发布需求分页列表
     */
    PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


    /**
     * 查询我参与的实训需求列表
     *
     * @param userId 用户ID
     * @param queryDTO 查询条件
     * @return 参与的实训需求分页列表
     */
    PageResult<DemandListVO> getMyJoinedPracticeList(Long userId, PracticeDemandQueryDTO queryDTO);


    /**
     * 根据ID获取需求基础信息
     *
     * @param demandId 需求ID
     * @return 需求基础实体信息
     */
    Demand getDemand(Long demandId);


    /**
     * 查询可申请公开需求详情
     *
     * @param demandId 需求ID
     * @return 公开需求详情信息
     */
    DemandPublicDetailVO getPublicDemandDetail(Long demandId);


    /**
     * 转换为申请需求分页列表
     *
     * @param demandIPage 需求分页数据
     * @param applyStatusMap 申请状态映射
     * @return 申请需求展示分页列表
     */
    PageResult<DemandListVO> convertToApplyPageResultList(IPage<Demand> demandIPage, Map<Long, DemandApplyList> applyStatusMap);


    /**
     * 转换为实践需求分页列表
     *
     * @param userId 用户ID
     * @param demandIPage 需求分页数据
     * @return 实训需求展示分页列表
     */
    PageResult<DemandListVO> convertToPracticePageResultList(Long userId, IPage<Demand> demandIPage);


    /**
     * 转换为我的发布需求分页列表
     *
     * @param demandIPage 需求分页数据
     * @return 我的发布需求展示分页列表
     */
    PageResult<DemandListVO> convertToMyPageResultList(IPage<Demand> demandIPage);


    /**
     * 转换为管理员查看的需求分页列表
     *
     * @param demandIPage 需求分页数据
     * @return 管理员需求展示分页列表
     */
    PageResult<DemandListVO> convertToAdminPageResultList(IPage<Demand> demandIPage);
}