package com.sc.qisi_system.module.demand.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;
import com.sc.qisi_system.module.demand.service.MinioService;
import com.sc.qisi_system.module.demand.service.DemandPublishService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/demand/publish")
@RequiredArgsConstructor
@RestController
@Validated
public class DemandPublishController {


    private final DemandPublishService demandPublishService;
    private final MinioService minioService;


    /**
     * 提交需求草稿接口
     * 角色: 发布者
     *
     * @param demandPublishDraftDTO 需求请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    @PostMapping("/draft")
    public Result submitDraft(
            @Valid @RequestBody DemandPublishDraftDTO demandPublishDraftDTO) {
        return Result.success(demandPublishService.submitDraft(demandPublishDraftDTO));
    }


    /**
     * 修改需求草稿接口
     *
     * @param demandUpdateDraftDTO 请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    @PutMapping("/update-draft")
    public Result updateDraft(
            @Valid @RequestBody DemandUpdateDraftDTO demandUpdateDraftDTO) {
        return Result.success(demandPublishService.updateDraft(demandUpdateDraftDTO));
    }


    /**
     * 提交审核接口
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 返回统一结果
     */
    @PostMapping("/submit-audit")
    public Result submitAudit(
            @NotNull(message = "需求ID不能为空")
            @RequestParam Long demandId) {
        return Result.success(demandPublishService.submitAudit(demandId));
    }


    /**
     * 撤销审核接口
     * 角色: 发布者
     *
     * @param demandId 撤销需求id
     * @return 统一返回结果
     */
    @DeleteMapping("/cancel-submit")
    public Result cancelSubmit(
            @NotBlank @RequestParam Long demandId) {
        return Result.success(demandPublishService.cancelSubmit(SecurityUtils.getCurrentUserId(),demandId));
    }


    /**
     * 需求附件上传接口
     *
     * @param demandId 需求ID
     * @param files    上传的文件
     * @return 文件信息/访问路径
     */
    @PostMapping("/batch-upload-attachment")
    public Result batchUploadAttachment(
            @NotNull(message = "需求ID不能为空")
            @RequestParam Long demandId,
            @RequestParam("files") MultipartFile[] files) throws Exception {
        return Result.success(minioService.batchUploadDemandAttachment(demandId, files));
    }


    /**
     * 删除需求附件接口
     *
     * @param attachmentId 附件ID
     * @return 操作结果
     */
    @DeleteMapping("/attachment/delete")
    public Result deleteAttachment(
            @NotNull(message = "附件ID不能为空") @RequestParam Long attachmentId) {
        minioService.deleteAttachment(attachmentId);
        return Result.success();
    }


    /**
     * 批量删除需求附件接口
     *
     * @param attachmentIds 需求附件id列表
     * @return 统一返回结果
     */
    @DeleteMapping("/attachment/delete/batch")
    public Result deleteBatchAttachment(
            @NotEmpty(message = "附件ID列表不能为空") @RequestBody List<Long> attachmentIds) {
        return Result.success("已删除" + minioService.deleteBatchAttachment(attachmentIds).size() + "个文件");
    }


}
