package com.sc.qisi_system.module.admin.service;

public interface AdminStatisticsService {


    /**
     * 查询管理员待审核需求数量接口
     * 角色: 管理员
     *
     * @author 郭双祎
     */
    String getPendingReviewDemandCount();
}
