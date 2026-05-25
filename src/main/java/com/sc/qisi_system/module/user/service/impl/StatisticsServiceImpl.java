package com.sc.qisi_system.module.user.service.impl;

import com.sc.qisi_system.module.admin.vo.AdminWorkbenchStatVO;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.demand.vo.DemandListVO;
import com.sc.qisi_system.module.user.service.StatisticsService;
import com.sc.qisi_system.module.user.vo.UserWorkbenchStatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {


    private final DemandService demandService;


    /**
     * 获取管理员工作台统计数据
     * 角色: 认领者、发布者
     *
     * @return 管理员工作台统计视图对象
     */
    @Override
    public AdminWorkbenchStatVO getWorkbenchStatistics() {
        return demandService.getWorkbenchStatistics();
    }


    /**
     * 获取普通用户工作台统计数据接口
     * 角色: 认领者、发布者
     *
     * @return 用户工作台统计视图对象
     */
    @Override
    public UserWorkbenchStatVO getUserWorkbenchStatistics() {
        return demandService.getUserWorkbenchStatistics();
    }


    /**
     * 获取最新发布的需求列表接口
     * 角色: 认领者、发布者
     *
     * @return 最新需求列表集合
     */
    @Override
    public List<DemandListVO> getLatestDemandList() {
        return demandService.getLatestDemandList();
    }
}
