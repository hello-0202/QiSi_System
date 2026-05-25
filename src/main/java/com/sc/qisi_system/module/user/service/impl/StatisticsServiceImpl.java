package com.sc.qisi_system.module.user.service.impl;

import com.sc.qisi_system.module.admin.vo.StatusDataVO;
import com.sc.qisi_system.module.demand.service.DemandService;
import com.sc.qisi_system.module.user.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {


    private final DemandService demandService;


    /**
     * 查询已发布需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    @Override
    public String getPublishedDemandCount() {
        return demandService.getPublishedDemandCount();
    }


    /**
     * 查询研究中需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    @Override
    public String getResearchingDemandCount() {
        return demandService.getResearchingDemandCount();
    }


    /**
     * 查询管理员待审核需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    @Override
    public String getPendingReviewDemandCount() {
        return demandService.getPendingReviewDemandCount();
    }


    /**
     * 查询需求状态分布统计接口
     *
     * @author 郭双祎
     */
    @Override
    public StatusDataVO getDemandStatusDistribution() {
        return demandService.getDemandStatusDistribution();
    }
}
