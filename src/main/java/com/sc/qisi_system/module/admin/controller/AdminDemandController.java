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

@RequestMapping("/admin/demand")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminDemandController {


    private final AdminDemandService adminDemandService;


    /**
     * 查询需求审核列表接口
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
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail")
    public Result getDemandDetail(
            @NotNull @RequestParam Long demandId) {
        return Result.success(adminDemandService.getDemandDetail(demandId));
    }


    /**
     * 审核需求接口
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
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public Result getDemandList(
            @Valid @RequestBody AdminDemandQueryDTO adminDemandQueryDTO) {
        // 业务逻辑待实现
        return Result.success(adminDemandService.getDemandList(adminDemandQueryDTO));
    }
}