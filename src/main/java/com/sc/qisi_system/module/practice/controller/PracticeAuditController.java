package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;
import com.sc.qisi_system.module.practice.service.PracticeAuditService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 实践需求审核操作控制器
 */
@RequestMapping("/api/practice/audit")
@RequiredArgsConstructor
@RestController
@Validated
public class PracticeAuditController {


    private final PracticeAuditService practiceAuditService;


    /**
     * 提交需求计划接口
     * 角色: 发布者
     *
     * @param demandPlanDTO 需求计划请求体
     * @return 统一返回结果
     */
    @PostMapping("/plan/submit")
    public Result submitDemandPlan(
            @Valid @RequestBody DemandPlanDTO demandPlanDTO) {
        practiceAuditService.submitDemandPlan(demandPlanDTO);
        return Result.success();
    }


    /**
     * 开始研究接口
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 统一返回结果
     */
    @PutMapping("/research/start")
    public Result startResearch(
            @NotNull @RequestParam Long demandId) {
        practiceAuditService.startResearch(demandId);
        return Result.success();
    }


    /**
     * 完成研究接口
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 统一返回结果
     */
    @PutMapping("/research/complete")
    public Result completeResearch(
            @NotNull @RequestParam Long demandId) {
        practiceAuditService.completeResearch(demandId);
        return Result.success();
    }


    /**
     * 关闭需求接口
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 统一返回结果
     */
    @PutMapping("/demand/close")
    public Result closeDemand(
            @NotNull @RequestParam Long demandId) {
        practiceAuditService.closeDemand(demandId);
        return Result.success();
    }


    /**
     * 重新提交审核接口
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 统一返回结果
     */
    @PutMapping("/audit/resubmit")
    public Result resubmitAudit(
            @NotNull @RequestParam Long demandId) {
        practiceAuditService.resubmitAudit(demandId);
        return Result.success();
    }


    /**
     * 踢出成员接口
     * 角色: 发布者
     *
     * @param memberChangeDTO 需求成员变更请求体
     * @return 统一返回结果
     */
    @DeleteMapping("/member/kick")
    public Result kickMember(
            @Valid @RequestBody MemberChangeDTO memberChangeDTO) {
        practiceAuditService.kickMember(memberChangeDTO);
        return Result.success();
    }


    /**
     * 发布者审核成员退出申请接口
     *
     * @param memberChangeDTO 需求成员变更请求体
     * @return 统一返回结果
     */
    @PostMapping("/quit/audit")
    public Result auditMemberQuitApply(
            @Valid @RequestBody MemberChangeDTO memberChangeDTO) {
        practiceAuditService.auditMemberQuitApply(memberChangeDTO);
        return Result.success();
    }
}