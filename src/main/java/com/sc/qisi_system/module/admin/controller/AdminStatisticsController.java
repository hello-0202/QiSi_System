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
     * 查询管理员待审核需求数量接口
     * 角色: 管理员
     *
     * @return 待审核需求数量
     * @author 郭双祎
     */
    @GetMapping("/pending-review-demand-count")
    public Result getPendingReviewDemandCount() {
        return Result.success(adminStatisticsService.getPendingReviewDemandCount());
    }
}
