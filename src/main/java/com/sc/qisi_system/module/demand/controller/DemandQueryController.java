package com.sc.qisi_system.module.demand.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.ApplicableDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.service.MinioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/demand/query")
@RequiredArgsConstructor
@RestController
@Validated
public class DemandQueryController {


    private final DemandQueryService demandQueryService;
    private final MinioService minioService;


    /**
     * 查询我的草稿列表接口
     *
     * @param pageNum  查询页数
     * @param pageSize 查询数量
     * @return 返回草稿列表
     */
    @GetMapping("/my-draft-list")
    public Result getDraftList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(demandQueryService.getDraftList(SecurityUtils.getCurrentUserId(), pageNum, pageSize));
    }


    /**
     * 条件查询我的需求列表接口
     *
     * @param myDemandQueryDTO 查询请求体
     * @return 我的需求列表
     */
    @PostMapping("/my-demand-list")
    public Result getDemandList(
            @RequestBody MyDemandQueryDTO myDemandQueryDTO) {
        return Result.success(demandQueryService.getMyDemandList(SecurityUtils.getCurrentUserId(), myDemandQueryDTO));
    }


    /**
     * 查询需求详情接口
     *
     * @param demandId 需求id
     * @return 需求完整信息
     */
    @GetMapping("/demand-detail")
    public Result getDemandDetail(
            @NotBlank(message = "需求id不能为空") @RequestParam Long demandId) {
        return Result.success(demandQueryService.getMyDemandDetail(demandId));
    }


    /**
     * 查看需求附件列表接口
     *
     * @param demandId 需求ID
     * @return 附件列表（文件名/大小/访问链接/上传时间等）
     */
    @GetMapping("/attachment/list")
    public Result getDemandAttachmentList(
            @NotNull(message = "需求ID不能为空") @RequestParam Long demandId) {
        return Result.success(minioService.getDemandAttachmentList(demandId));
    }


    /**
     * 条件查询可申请的需求列表接口
     *
     * @param applicableDemandQueryDTO 查询请求体
     * @return 可申请需求的列表
     */
    @PostMapping("/applicable-demand-list")
    public Result getApplicableList(
            @Valid @RequestBody ApplicableDemandQueryDTO applicableDemandQueryDTO) {
        return Result.success(demandQueryService.getApplicableList(SecurityUtils.getCurrentUserId(),applicableDemandQueryDTO));
    }


    /**
     * 查询可申请需求详情接口
     *
     * @param demandId 需求id
     * @return 指定可申请需求详情
     */
    @GetMapping("applicable-demand-detail")
    public Result getApplicableDemandDetail(
            @NotNull @RequestParam Long demandId){
        return Result.success(demandQueryService.getDemandReceiverDetail(demandId));
    }


}
