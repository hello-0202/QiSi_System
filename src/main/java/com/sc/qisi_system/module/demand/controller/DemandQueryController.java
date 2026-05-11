package com.sc.qisi_system.module.demand.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.minio.service.MinioService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 需求查询控制器
 * 功能: 我的草稿查询、我的需求查询、需求详情查看、需求附件查看、公开需求详情查询
 */
@RequestMapping("/api/demand/query")
@RequiredArgsConstructor
@RestController
@Validated
public class DemandQueryController {


    private final DemandQueryService demandQueryService;
    private final MinioService minioService;


    /**
     * 查询我的草稿列表接口
     * 角色: 发布者
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 草稿分页列表
     */
    @GetMapping("/my-draft-list")
    public Result getDraftList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(demandQueryService.getDraftList(SecurityUtils.getCurrentUserId(), pageNum, pageSize));
    }


    /**
     * 条件查询我的需求列表接口
     * 角色: 发布者
     *
     * @param myDemandQueryDTO 需求查询条件
     * @return 我的需求分页列表
     */
    @PostMapping("/my-demand-list")
    public Result getDemandList(
            @RequestBody MyDemandQueryDTO myDemandQueryDTO) {
        return Result.success(demandQueryService.getMyDemandList(SecurityUtils.getCurrentUserId(), myDemandQueryDTO));
    }


    /**
     * 查询我的需求详情接口
     * 角色: 认领者、发布者
     *
     * @param demandId 需求ID
     * @return 需求完整信息
     */
    @GetMapping("/demand-detail")
    public Result getDemandDetail(
            @NotNull(message = "需求id不能为空") @RequestParam Long demandId) {
        return Result.success(demandQueryService.getMyDemandDetail(demandId));
    }


    /**
     * 查看需求附件列表接口
     * 角色: 认领者、发布者
     *
     * @param demandId 需求ID
     * @return 附件信息列表
     */
    @GetMapping("/attachment/list")
    public Result getDemandAttachmentList(
            @NotNull(message = "需求ID不能为空") @RequestParam Long demandId) {
        return Result.success(minioService.getDemandAttachmentList(demandId));
    }


    /**
     * 查询可申请公开需求详情接口
     * 角色: 认领者、发布者
     *
     * @param demandId 需求ID
     * @return 公开需求详情信息
     */
    @GetMapping("/apply-demand-detail")
    public Result getApplyDemandDetail(
            @NotNull @RequestParam Long demandId){
        return Result.success(demandQueryService.getPublicDemandDetail(demandId));
    }
}