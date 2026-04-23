package com.sc.qisi_system.module.apply.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.apply.dto.ApplyUpdateDTO;
import com.sc.qisi_system.module.apply.dto.DemandApplyDTO;
import com.sc.qisi_system.module.apply.service.ApplyOperateService;
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


    private final ApplyOperateService applyOperateService;


    /**
     * 提交需求申请接口
     *
     * @param demandApplyDTO 请求体
     * @return 统一返回结果
     */
    @PostMapping("/submit")
    public Result submitApply(
            @Valid @RequestBody DemandApplyDTO demandApplyDTO) {
        applyOperateService.submitApply(SecurityUtils.getCurrentUserId(), demandApplyDTO);
        return Result.success();
    }


    /**
     * 修改申请信息接口
     *
     * @param applyUpdateDTO 请求体
     * @return 统一返回结果
     */
    @PutMapping("/update")
    public Result updateApply(@RequestBody @Valid ApplyUpdateDTO applyUpdateDTO) {
        applyOperateService.updateApply(SecurityUtils.getCurrentUserId(),applyUpdateDTO);
        return Result.success();
    }


    /**
     * 取消认领申请接口
     *
     * @param demandId 需求id
     * @return 统一返回结果
     */
    @GetMapping("/cancel")
    public Result cancelApply(
            @NotNull @RequestParam Long demandId) {
        applyOperateService.cancelApply(SecurityUtils.getCurrentUserId(), demandId);
        return Result.success();
    }
}
