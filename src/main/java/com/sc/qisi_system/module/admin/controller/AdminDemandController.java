package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.dto.AdminDemandQueryDTO;
import com.sc.qisi_system.module.admin.dto.AuditDemandDTO;
import com.sc.qisi_system.module.admin.service.AdminDemandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 需求管理控制器
 * 功能: 需求审核、需求查询、需求详情查看等管理员操作
 */
@RequestMapping("/api/admin/demand")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminDemandController {


    private final AdminDemandService adminDemandService;


    /**
     * 查询需求审核列表接口
     * 角色: 管理员
     *
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 需求审核分页列表
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/audit-list")
    public Result getDemandAuditList(
            @NotNull @RequestParam Integer pageNum,
            @NotNull @RequestParam Integer pageSize) {
        return Result.success(adminDemandService.getDemandAuditList(pageNum, pageSize));
    }


    /**
     * 查询需求详情接口
     * 角色: 管理员
     *
     * @param demandId 需求ID
     * @return 需求详细信息
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail")
    public Result getDemandDetail(
            @NotNull @RequestParam Long demandId) {
        return Result.success(adminDemandService.getDemandDetail(demandId));
    }


    /**
     * 审核需求接口
     * 角色: 管理员
     *
     * @param auditDemandDTO 审核请求参数
     * @return 审核操作结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/audit")
    public Result auditDemand(
            @Valid @RequestBody AuditDemandDTO auditDemandDTO) {
        adminDemandService.auditDemand(auditDemandDTO);
        return Result.success();
    }


    /**
     * 条件查询所有需求接口
     * 角色: 管理员
     *
     * @param adminDemandQueryDTO 需求查询条件
     * @return 需求列表数据
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/list")
    public Result getDemandList(
            @Valid @RequestBody AdminDemandQueryDTO adminDemandQueryDTO) {
        return Result.success(adminDemandService.getDemandList(adminDemandQueryDTO));
    }
}