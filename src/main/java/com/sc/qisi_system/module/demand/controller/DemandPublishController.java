package com.sc.qisi_system.module.demand.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.DemandPublishDraftDTO;
import com.sc.qisi_system.module.demand.dto.DemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.DemandUpdateDraftDTO;
import com.sc.qisi_system.module.demand.service.MinioService;
import com.sc.qisi_system.module.demand.service.PublishService;
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


    private final PublishService publishService;

    private final MinioService minioService;


    /**
     * 提交需求草稿接口
     *
     * @param demandPublishDraftDTO 需求请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    @PostMapping("/draft")
    public Result submitDraft(
            @Valid @RequestBody DemandPublishDraftDTO demandPublishDraftDTO) {
        return publishService.submitDraft(demandPublishDraftDTO);
    }


    /**
     * 修改需求草稿接口
     *
     * @param demandUpdateDraftDTO 请求参数
     * @return 成功返回草稿ID，失败返回错误信息
     */
    @PostMapping("/update-draft")
    public Result updateDraft(
            @Valid @RequestBody DemandUpdateDraftDTO demandUpdateDraftDTO) {
        return publishService.updateDraft(demandUpdateDraftDTO);
    }


    /**
     * 提交审核接口
     *
     * @param demandId 需求id
     * @return 返回统一结果
     */
    @PostMapping("/submit-audit")
    public Result submitAudit(
            @NotNull(message = "需求ID不能为空")
            @RequestParam Long demandId) {
        return publishService.submitAudit(demandId);
    }


    /**
     * 查询草稿列表接口
     *
     * @param pageNum  查询页数
     * @param pageSize 查询数量
     * @return 返回草稿列表
     */
    @GetMapping("/draft-list")
    public Result getDraftList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return publishService.getDraftList(SecurityUtils.getCurrentUserId(), pageNum, pageSize);
    }


    /**
     * 条件查询需求列表
     * @param demandQueryDTO
     * @return
     */
    @GetMapping("/list")
    public Result getDemandList(
            @RequestBody DemandQueryDTO demandQueryDTO) {
        return null;
    }


    /**
     * 查询需求详情接口
     *
     * @param demandId 需求id
     * @return 需求完整信息
     */
    @GetMapping("/demand-detail")
    public Result getDemandDetail(
            @NotNull(message = "需求id不能为空") @RequestParam Long demandId) {
        return publishService.getDemandDetail(demandId);
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
        return minioService.batchUploadDemandAttachment(demandId, files);
    }


    /**
     * 查看需求附件列表
     *
     * @param demandId 需求ID
     * @return 附件列表（文件名/大小/访问链接/上传时间等）
     */
    @GetMapping("/attachment/list")
    public Result getDemandAttachmentList(
            @NotBlank(message = "需求ID不能为空") @RequestParam Long demandId) {
        return minioService.getDemandAttachmentList(demandId);
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
        return minioService.deleteAttachment(attachmentId);
    }


    /**
     * 批量删除需求附件接口
     * @param attachmentIds 需求附件id列表
     * @return 统一返回结果
     */
    @DeleteMapping("/attachment/delete/batch")
    public Result deleteBatchAttachment(
            @NotEmpty(message = "附件ID列表不能为空") @RequestBody List<Long> attachmentIds) {
        return minioService.deleteBatchAttachment(attachmentIds);
    }

}
