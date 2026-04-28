package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.practice.dto.DemandPlanDTO;
import com.sc.qisi_system.module.practice.dto.DemandProgressDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeDTO;
import com.sc.qisi_system.module.practice.service.PracticeExecuteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 实践需求执行控制器
 */
@RequestMapping("/api/practice/execute")
@RequiredArgsConstructor
@RestController
@Validated
public class PracticeExecuteController {


    private final PracticeExecuteService practiceExecuteService;


    /**
     * 提交需求日志接口
     *
     * @param demandProgressDTO 需求日志请求体
     * @return 统一返回结果
     */
    @PostMapping("/log")
    public Result submitDemandLog(DemandProgressDTO demandProgressDTO) {
        practiceExecuteService.submitDemandLog(demandProgressDTO);
        return Result.success();
    }


    /**
     * 提交需求附件接口
     *
     * @return 统一返回结果
     */
    @PostMapping("/attachment")
    public Result batchUploadProgressAttachments(
            @NotNull(message = "需求ID不能为空")
            @RequestParam Long demandId,
            @RequestParam("files") MultipartFile[] files) throws Exception {
        return Result.success(practiceExecuteService.batchUploadProgressAttachments(demandId,files));
    }


    /**
     * 更新需求计划接口
     *
     * @param demandPlanDTO 需求计划请求体
     * @return 统一返回结果
     */
    @PutMapping("/plan")
    public Result updateDemandPlan(
            @Valid @RequestBody DemandPlanDTO demandPlanDTO) {
        practiceExecuteService.updateDemandPlan(demandPlanDTO);
        return Result.success();
    }


    /**
     * 删除【实践进度】单个附件
     */
    @DeleteMapping("/progress-attachment/delete")
    public Result deleteProgressAttachment(
            @NotNull @RequestParam Long attachmentId) {
        practiceExecuteService.deleteProgressAttachment(attachmentId);
        return Result.success();
    }

    /**
     * 批量删除【实践进度】附件
     */
    @DeleteMapping("/progress-attachment/delete/batch")
    public Result deleteBatchProgressAttachment(
            @NotNull @RequestBody List<Long> attachmentIds) {
        practiceExecuteService.deleteBatchProgressAttachment(attachmentIds);
        return Result.success();
    }


    /**
     * 主动申请退出需求接口
     *
     * @param memberChangeDTO 需求成员变更请求体
     * @return 统一返回结果
     */
    @PostMapping("/quit")
    public Result applyQuitDemand(
            @Valid @RequestBody MemberChangeDTO memberChangeDTO) {
        practiceExecuteService.applyQuitDemand(memberChangeDTO);
        return Result.success();
    }
}