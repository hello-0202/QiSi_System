package com.sc.qisi_system.module.apply.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.apply.dto.AuditApplyDTO;
import com.sc.qisi_system.module.apply.service.ApplyAuditService;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 需求认领审核
 */
@RequestMapping("/api/demand/apply/audit")
@RequiredArgsConstructor
@RestController
@Validated
public class ApplyAuditController {


    private final ApplyQueryService applyQueryService;
    private final ApplyAuditService applyAuditService;


    /**
     * 查看指定需求的申请成员列表接口
     *
     * @param demandId 需求id
     * @return 统一返回结果
     */
    @GetMapping("/demand/member-list/")
    public Result getApplyMemberList(
            @NotNull @RequestParam Long demandId) {
        return Result.success(applyQueryService.getApplyMemberList(SecurityUtils.getCurrentUserId(),demandId));
    }


    /**
     * 查询申请成员的详细信息接口
     *
     * @param userId 用户id
     * @return 用户详情信息
     */
    @GetMapping("/demand/apply/detail")
    public Result getApplyMemberDetail(
            @NotNull @RequestParam Long userId) {
        return Result.success(applyQueryService.getApplyMemberDetail(userId));
    }


    /**
     * 查询指定成员的申请信息接口
     */
    @GetMapping("/demand/apply/info")
    public Result getApplyDetail(
            @NotNull @RequestParam Long applyId) {
        return Result.success(applyQueryService.getApplyDetail(applyId));
    }


    /**
     * 审核通过申请成员
     */
    @PostMapping("/select")
    public Result approveApplyMember(
            @NotBlank @RequestBody AuditApplyDTO applyDTO) {
        applyAuditService.approveApplyMember(SecurityUtils.getCurrentUserId(),applyDTO);
        return Result.success();
    }


    /**
     * 审核拒绝申请成员
     */
    @PostMapping("/reject")
    public Result rejectApply(
            @NotBlank @RequestBody AuditApplyDTO auditApplyDTO) {
        applyAuditService.rejectApplyMember(SecurityUtils.getCurrentUserId(), auditApplyDTO);
        return Result.success();
    }
}
