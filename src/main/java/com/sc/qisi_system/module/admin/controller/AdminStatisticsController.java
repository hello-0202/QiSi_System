package com.sc.qisi_system.module.admin.controller;


import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据统计后台控制器
 * 功能: 统计数据
 */
@RequestMapping("/api/admin/statistics")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminStatisticsController {


    private final AdminStatisticsService adminStatisticsService;


    /**
     * 查询已发布需求数量接口
     * 角色: 管理员
     *
     * @return 已发布需求数量
     */
    @GetMapping("/published-demand-count")
    public Result getPublishedDemandCount() {
        return Result.success(adminStatisticsService.getPublishedDemandCount());
    }


    /**
     * 查询研究中需求数量接口
     * 角色: 管理员
     *
     * @return 研究中需求数量
     */
    @GetMapping("/researching-demand-count")
    public Result getResearchingDemandCount() {
        return Result.success(adminStatisticsService.getResearchingDemandCount());
    }


    /**
     * 查询管理员待审核需求数量接口
     * 角色: 管理员
     *
     * @return 待审核需求数量
     */
    @GetMapping("/pending-review-demand-count")
    public Result getPendingReviewDemandCount() {
        return Result.success(adminStatisticsService.getPendingReviewDemandCount());
    }


    /**
     * 查询需求状态分布统计接口
     * 角色: 管理员
     *
     * @return 各状态需求数量（饼图数据）
     */
    @GetMapping("/demand-status-distribution")
    public Result getDemandStatusDistribution() {
        return Result.success(adminStatisticsService.getDemandStatusDistribution());
    }
}
