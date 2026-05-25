package com.sc.qisi_system.module.demand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sc.qisi_system.module.admin.dto.DateQueryDTO;
import com.sc.qisi_system.module.admin.vo.DemandCategoryVO;
import com.sc.qisi_system.module.admin.vo.DemandDailyTrendVO;
import com.sc.qisi_system.module.demand.entity.Demand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DemandMapper extends BaseMapper<Demand> {


    // 管理员工作台 - 今日统计
    long countAdminTotalDemand();
    long countAdminOngoingDemand();
    long countAdminCompletedDemand();
    long countAdminPendingReviewDemand();

    // 管理员工作台 - 昨日统计（趋势用）
    long countAdminTotalDemandYesterday();
    long countAdminOngoingDemandYesterday();
    long countAdminCompletedDemandYesterday();
    long countAdminPendingReviewDemandYesterday();


    // 用户工作台 - 我的申请
    Long countUserMyApply(Long userId);
    Long countUserMyApplyYesterday(Long userId);
    // 用户工作台 - 进行中
    Long countUserOngoing(Long userId);
    Long countUserOngoingYesterday(Long userId);
    // 用户工作台 - 已完成
    Long countUserCompleted(Long userId);
    Long countUserCompletedYesterday(Long userId);


    // 管理员数据统计 - 总数据
    long countStatTotalDemand();           // 总需求数
    long countStatPendingReview();         // 待审核
    long countStatPublished();             // 已发布
    long countStatOngoing();               // 进行中
    long countStatCompleted();             // 已完成


    List<DemandDailyTrendVO> selectDemandTrendByDate(@Param("query") DateQueryDTO query);


    // 按分类统计需求数量
    List<DemandCategoryVO> countDemandByCategory();
}
