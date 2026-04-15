package com.sc.qisi_system.module.apply.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;
import com.sc.qisi_system.module.apply.service.DemandApplyService;
import com.sc.qisi_system.module.demand.dto.ApplicableDemandQueryDTO;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 需求认领申请控制器
 */
@RequestMapping("/api/demand/apply")
@RequiredArgsConstructor
@RestController
@Validated
public class ApplyController {


    private final DemandApplyService demandApplyService;
    private final DemandQueryService demandQueryService;


    /**
     * 查询可申请的需求列表接口
     *
     * @param applicableDemandQueryDTO 查询请求体
     * @return 可申请需求的列表
     */
    @PostMapping("/applicable-demand-list")
    public Result getApplicableList(
            @Valid @RequestBody ApplicableDemandQueryDTO applicableDemandQueryDTO) {
        return Result.success(demandQueryService.getApplicableList(applicableDemandQueryDTO));
    }


    /**
     * 查询可申请需求详情接口
     *
     * @param demandId 需求id
     * @return 指定可申请需求详情
     */
    @GetMapping
    public Result getApplicableDemandDetail(
            @NotNull @RequestParam Long demandId){
        return Result.success(demandQueryService.getApplicableDemandDetail(demandId));
    }


    /**
     * 提交需求认领申请接口
     */
    @PostMapping("/submit")
    public Result submitApply(
            @Valid @RequestBody DemandApplyDTO demandApplyDTO) {
        demandApplyService.submitApply(SecurityUtils.getCurrentUserId(), demandApplyDTO);
        return Result.success();
    }


    //TODO
    /**
     * 分页查询我的认领申请列表接口
     */
    @GetMapping("/my-list")
    public Result getMyApplyList() {
        return null;
    }


    //TODO
    /**
     * 查询申请详情接口
     */
    @GetMapping("/detail")
    public Result getApplyDetail() {
        return null;
    }


    /**
     * 取消认领申请接口
     */
    @DeleteMapping("/cancel")
    public Result cancelApply(
            @NotNull @RequestParam Long demandId) {
        demandApplyService.cancelApply(SecurityUtils.getCurrentUserId(), demandId);
        return Result.success();
    }


}
