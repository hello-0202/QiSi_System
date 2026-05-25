package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 数据统计后台控制器
 * 功能: 统计数据
 */
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@RestController
@Validated
public class StatisticsController {


    private final StatisticsService statisticsService;


    /**
     * 查询已发布需求数量接口
     * 角色: 管理员
     *
     * @return 已发布需求数量
     * @author 郭双祎
     */
    @GetMapping("/published-demand-count")
    public Result getPublishedDemandCount() {
        return Result.success(statisticsService.getPublishedDemandCount());
    }


    /**
     * 查询研究中需求数量接口
     * 角色: 管理员
     *
     * @return 研究中需求数量
     * @author 郭双祎
     */
    @GetMapping("/researching-demand-count")
    public Result getResearchingDemandCount() {
        return Result.success(statisticsService.getResearchingDemandCount());
    }


    /**
     * 查询管理员待审核需求数量接口
     * 角色: 管理员
     *
     * @return 待审核需求数量
     * @author 郭双祎
     */
    @GetMapping("/pending-review-demand-count")
    public Result getPendingReviewDemandCount() {
        return Result.success(statisticsService.getPendingReviewDemandCount());
    }


    /**
     * 查询需求状态分布统计接口
     * 角色: 管理员
     *
     * @return 各状态需求数量
     * @author 郭双祎
     */
    @GetMapping("/demand-status-distribution")
    public Result getDemandStatusDistribution() {
        return Result.success(statisticsService.getDemandStatusDistribution());
    }
}
