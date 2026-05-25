package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.module.admin.dto.DateQueryDTO;
import com.sc.qisi_system.module.admin.vo.AdminStatVO;
import com.sc.qisi_system.module.admin.vo.DemandCategoryVO;
import com.sc.qisi_system.module.admin.vo.DemandDailyTrendVO;

import java.util.List;

/**
 * 管理员数据统计服务接口
 * 功能: 管理员数据大盘统计、需求趋势图表、需求分类分布等全局数据统计业务
 */
public interface AdminStatisticsService {


    /**
     * 获取管理员数据大盘统计信息（总需求、待审核、已发布、进行中、已完成、用户数、总申请数、完成率）
     *
     * @return 管理员数据大盘视图对象
     */
    AdminStatVO getAdminDataStatistics();


    /**
     * 根据时间范围获取需求每日趋势数据（折线图专用）
     *
     * @param dateQueryDTO 时间查询条件
     * @return 每日需求数量趋势集合
     */
    List<DemandDailyTrendVO> getDemandTrend(DateQueryDTO dateQueryDTO);


    /**
     * 获取需求分类分布统计数据（饼图专用）
     *
     * @return 需求分类统计集合
     */
    List<DemandCategoryVO> getDemandCategoryDistribution();
}