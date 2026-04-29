package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/demand")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminDemandController {


    //TODO
    /**
     * 查询需求审核列表接口
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/audit-list")
    public Result getDemandAuditList() {
        // 业务逻辑待实现
        return Result.success(null);
    }


    //TODO
    /**
     * 查询需求详情接口
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail")
    public Result getDemandDetail() {
        // 业务逻辑待实现
        return Result.success(null);
    }


    //TODO
    /**
     * 审核需求接口
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/audit")
    public Result auditDemand() {
        // 业务逻辑待实现
        return Result.success();
    }


    //TODO
    /**
     * 条件查询所有需求接口
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public Result getDemandList() {
        // 业务逻辑待实现
        return Result.success(null);
    }

}