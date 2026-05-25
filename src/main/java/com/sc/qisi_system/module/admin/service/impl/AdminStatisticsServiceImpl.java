package com.sc.qisi_system.module.admin.service.impl;

import com.sc.qisi_system.module.admin.service.AdminStatisticsService;
import com.sc.qisi_system.module.demand.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService {


    private final DemandService demandService;


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
}
