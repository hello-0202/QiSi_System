package com.sc.qisi_system.module.demand.controller;


import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.DemandQueryDTO;
import com.sc.qisi_system.module.demand.service.DemandQueryService;
import com.sc.qisi_system.module.demand.service.MinioService;
import com.sc.qisi_system.module.demand.service.DemandPublishService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/demand/query")
@RequiredArgsConstructor
@RestController
@Validated
public class DemandQueryController {


    private final DemandPublishService demandPublishService;
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
        return demandQueryService.getDraftList(SecurityUtils.getCurrentUserId(), pageNum, pageSize);
    }


    /**
     * 条件查询我的需求列表接口
     *
     * @param demandQueryDTO 查询请求体
     * @return 统一返回结果
     */
    @GetMapping("/my-demand-list")
    public Result getDemandList(
            @RequestBody DemandQueryDTO demandQueryDTO) {
        return demandQueryService.getDemandList(SecurityUtils.getCurrentUserId(), demandQueryDTO);
    }


    //TODO
    /**
     * 查询可申请的需求列表
     */
    @GetMapping("/applicable-demand-list")
    public Result getApplicableList(){
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
            @NotBlank(message = "需求id不能为空") @RequestParam Long demandId) {
        return demandQueryService.getDemandDetail(demandId);
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





}
