package com.sc.qisi_system.module.apply.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.apply.dto.ApplyUpdateDTO;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;
import com.sc.qisi_system.module.apply.dto.MyApplyQueryDTO;
import com.sc.qisi_system.module.apply.service.ApplyOperateService;
import com.sc.qisi_system.module.apply.service.ApplyQueryService;
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
public class ApplyOperateController {


    private final ApplyQueryService applyQueryService;
    private final ApplyOperateService applyOperateService;
    private final DemandQueryService demandQueryService;


    /**
     * 提交需求申请接口
     */
    @PostMapping("/submit")
    public Result submitApply(
            @Valid @RequestBody DemandApplyDTO demandApplyDTO) {
        applyOperateService.submitApply(SecurityUtils.getCurrentUserId(), demandApplyDTO);
        return Result.success();
    }


    /**
     * 条件查询我申请的需求列表接口
     */
    @GetMapping("/my-list")
    public Result getMyApplyDemandList(
            @RequestBody MyApplyQueryDTO queryDTO) {
        return Result.success(demandQueryService.getMyApplyDemandList(SecurityUtils.getCurrentUserId(),queryDTO));
    }


    /**
     * 查询申请详情
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
     * 修改申请信息接口
     */
    @PutMapping("/update")
    public Result updateApply(@RequestBody @Valid ApplyUpdateDTO applyUpdateDTO) {
        applyOperateService.updateApply(SecurityUtils.getCurrentUserId(),applyUpdateDTO);
        return Result.success();
    }


    /**
     * 取消认领申请接口
     */
    @GetMapping("/cancel")
    public Result cancelApply(
            @NotNull @RequestParam Long demandId) {
        applyOperateService.cancelApply(SecurityUtils.getCurrentUserId(), demandId);
        return Result.success();
    }
}
