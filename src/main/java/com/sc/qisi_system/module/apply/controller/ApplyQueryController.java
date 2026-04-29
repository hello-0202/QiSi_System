package com.sc.qisi_system.module.apply.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
import com.sc.qisi_system.module.demand.dto.ApplyDemandQueryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 需求申请查询控制器
 */
@RequestMapping("/api/demand/apply/query")
@RequiredArgsConstructor
@RestController
@Validated
public class ApplyQueryController {


    private final ApplyQueryService applyQueryService;


    /**
     * 条件查询可申请的需求列表接口
     *
     * @param applyDemandQueryDTO 查询请求体
     * @return 可申请需求的列表
     */
    @PostMapping("/apply-demand-list")
    public Result getApplyList(
            @Valid @RequestBody ApplyDemandQueryDTO applyDemandQueryDTO) {
        return Result.success(applyQueryService.getApplyList(SecurityUtils.getCurrentUserId(), applyDemandQueryDTO));
    }


    /**
     * 条件查询我申请的需求列表接口
     *
     * @param queryDTO 请求体
     * @return 我申请的需求列表信息
     */
    @GetMapping("/my-list")
    public Result getMyApplyDemandList(
            @RequestBody MyApplyQueryDTO queryDTO) {
        return Result.success(applyQueryService.getMyApplyDemandList(SecurityUtils.getCurrentUserId(), queryDTO));
    }


    /**
     * 查询申请详情接口
     *
     * @param applyId 需求申请id
     * @return 需求详情
     */
    @GetMapping("/my-detail")
    public Result getMyApplyDetail(
            @NotNull @RequestParam Long applyId) {
        return Result.success(applyQueryService.getMyApplyDetail(applyId));
    }


    /**
     * 查看指定需求的申请成员列表接口
     *
     * @param demandId 需求id
     * @return 统一返回结果
     */
    @GetMapping("/demand/member-list")
    public Result getApplyMemberList(
            @NotNull @RequestParam Long demandId) {
        return Result.success(applyQueryService.getApplyMemberList(SecurityUtils.getCurrentUserId(), demandId));
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
        return Result.success(applyQueryService.getMemberDetail(userId));
    }


    /**
     * 查询指定成员的申请信息接口
     *
     * @param applyId 申请信息id
     * @return 申请详细信息
     */
    @GetMapping("/demand/apply/info")
    public Result getApplyDetail(
            @NotNull @RequestParam Long applyId) {
        return Result.success(applyQueryService.getApplyDetail(applyId));
    }
}