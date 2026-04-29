package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.apply.dto.AuditApplyDTO;
import com.sc.qisi_system.module.practice.service.ApplyAuditService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 需求认领审核控制器
 */
@RequestMapping("/api/demand/apply/audit")
@RequiredArgsConstructor
@RestController
@Validated
public class ApplyAuditController {


    private final ApplyAuditService applyAuditService;


    /**
     * 审核通过申请成员接口
     *
     * @param auditApplyDTO 请求体
     * @return 统一返回结果
     */
    @PostMapping("/select")
    public Result approveApplyMember(
            @NotBlank @RequestBody AuditApplyDTO auditApplyDTO) {
        applyAuditService.approveApplyMember(SecurityUtils.getCurrentUserId(),auditApplyDTO);
        return Result.success();
    }


    /**
     * 审核拒绝申请成员
     *
     * @param auditApplyDTO 请求体
     * @return 统一返回结果
     */
    @PostMapping("/reject")
    public Result rejectApply(
            @NotBlank @RequestBody AuditApplyDTO auditApplyDTO) {
        applyAuditService.rejectApplyMember(SecurityUtils.getCurrentUserId(), auditApplyDTO);
        return Result.success();
    }
}
