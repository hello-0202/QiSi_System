package com.sc.qisi_system.module.demand.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.DateQueryDTO;
import com.sc.qisi_system.module.admin.vo.AdminStatVO;
import com.sc.qisi_system.module.admin.vo.AdminWorkbenchStatVO;
import com.sc.qisi_system.module.admin.vo.DemandCategoryVO;
import com.sc.qisi_system.module.admin.vo.DemandDailyTrendVO;
import com.sc.qisi_system.module.demand.domain.DemandApplyList;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.entity.Demand;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.demand.vo.DemandPublicDetailVO;
import com.sc.qisi_system.module.user.vo.UserWorkbenchStatVO;

import java.util.List;
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
     * 查询我的发布需求列表
     *
     * @param userId 用户ID
     * @param myDemandQueryDTO 查询条件
     * @return 我的发布需求分页列表
     */
    PageResult<DemandListVO> getMyDemandList(Long userId, MyDemandQueryDTO myDemandQueryDTO);


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


    /**
     * 获取管理员工作台统计数据（总需求、待审核、进行中、已完成等）
     *
     * @return 管理员工作台统计视图对象
     */
    AdminWorkbenchStatVO getWorkbenchStatistics();


    /**
     * 获取普通用户工作台统计数据（我的申请、进行中、已完成、待处理通知）
     *
     * @return 用户工作台统计视图对象
     */
    UserWorkbenchStatVO getUserWorkbenchStatistics();


    /**
     * 获取管理员数据统计界面数据（总需求、待审核、已发布、进行中、已完成、用户数、申请数、完成率）
     *
     * @return 管理员数据统计视图对象
     */
    AdminStatVO getAdminDataStatistics();


    /**
     * 根据日期范围查询需求每日趋势统计（折线图数据）
     *
     * @param dateQueryDTO 日期查询条件
     * @return 每日需求数量趋势集合
     */
    List<DemandDailyTrendVO> selectDemandTrendByDate(DateQueryDTO dateQueryDTO);


    /**
     * 获取需求分类分布统计数据（饼图数据）
     *
     * @return 需求分类统计集合
     */
    List<DemandCategoryVO> getDemandCategoryDistribution();


    /**
     * 获取最新发布的需求列表（默认最新10条，用于管理员统计页展示）
     *
     * @return 最新需求列表集合
     */
    List<DemandListVO> getLatestDemandList();
}