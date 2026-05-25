package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.admin.vo.AdminWorkbenchStatVO;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.user.vo.UserWorkbenchStatVO;

import java.util.List;


/**
 * 工作台服务接口
 * 功能: 管理员/用户工作台统计、数据大盘统计、图表趋势、分类分布、最新需求列表等数据统计业务
 */
public interface StatisticsService {


    /**
     * 获取管理员工作台统计数据
     *
     * @return 管理员工作台统计视图对象
     */
    AdminWorkbenchStatVO getWorkbenchStatistics();


    /**
     * 获取用户工作台统计数据
     *
     * @return 用户工作台统计视图对象
     */
    UserWorkbenchStatVO getUserWorkbenchStatistics();


    /**
     * 获取最新已发布需求列表
     *
     * @return 最新需求列表
     */
    List<DemandListVO> getLatestDemandList();
}