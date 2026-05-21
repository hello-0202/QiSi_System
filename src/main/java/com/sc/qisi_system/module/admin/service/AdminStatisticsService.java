package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.module.admin.vo.StatusDataVO;

public interface AdminStatisticsService {

    /**
     * 查询已发布需求数量接口
     * 角色: 管理员
     */
    Long getPublishedDemandCount();

    /**
     * 查询研究中需求数量接口
     * 角色: 管理员
     */
    Long getResearchingDemandCount();

    /**
     * 查询管理员待审核需求数量接口
     * 角色: 管理员
     */
    Long getPendingReviewDemandCount();

    /**
     * 查询需求状态分布统计接口
     * 角色: 管理员
     */
    StatusDataVO getDemandStatusDistribution();
}
