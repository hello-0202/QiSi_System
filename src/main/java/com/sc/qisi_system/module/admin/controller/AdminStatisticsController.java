package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.dto.DateQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
     * 获取管理员数据卡片统计信息接口（总需求、待审核、已发布、进行中、已完成、用户数、总申请数、完成率）
     * 角色: 管理员
     *
     * @return 8张统计卡片
     */
    @GetMapping("/admin-data-statistics")
    public Result getAdminDataStatistics() {
        return Result.success(adminStatisticsService.getAdminDataStatistics());
    }


    /**
     * 根据时间范围获取需求每日趋势数据接口
     * 角色: 管理员
     *
     * @param query 时间查询条件
     * @return 每日需求数量趋势集合
     */
    @PostMapping("/demand-trend")
    public Result getDemandTrend(
            @RequestBody  DateQueryDTO query) {
        return Result.success(adminStatisticsService.getDemandTrend(query));
    }


    /**
     * 获取需求分类分布统计数据接口
     * 角色: 管理员
     *
     * @return 需求分类统计集合
     */
    @GetMapping("/demand-category")
    public Result getDemandCategoryDistribution() {
        return Result.success(adminStatisticsService.getDemandCategoryDistribution());
    }
}
