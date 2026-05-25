package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 工作台数据统计控制器
 * 功能: 统计数据
 */
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@RestController
@Validated
public class StatisticsController {


    private final StatisticsService statisticsService;


    /**
     * 查询管理员工作台统计数据接口
     * 角色: 管理员
     *
     * @return 工作台统计数据（需求总数、待审核数量、进行中数量、已完成数量）
     */
    @GetMapping("/admin/workbench-statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public Result getWorkbenchStatistics() {
        return Result.success(statisticsService.getWorkbenchStatistics());
    }


    /**
     * 查询用户工作台统计数据接口
     * 角色: 认领者、发布者
     *
     * @return 工作台统计：我的申请、进行中、已完成、待处理通知
     */
    @GetMapping("/user-workbench-statistics")
    public Result getUserWorkbenchStatistics() {
        return Result.success(statisticsService.getUserWorkbenchStatistics());
    }


    /**
     * 获取最新已发布需求列表
     * 角色: 认领者、发布者、管理员
     *
     * @return 最新需求列表
     */
    @GetMapping("/latest-demand")
    public Result getLatestDemand() {
        return Result.success(statisticsService.getLatestDemandList());
    }
}
