package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeLogDTO;
import com.sc.qisi_system.module.practice.dto.QueryDemandProgressLogDTO;
import com.sc.qisi_system.module.practice.service.PracticeQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 实践信息查询控制器
 */
@RequestMapping("/api/practice/query")
@RequiredArgsConstructor
@RestController
@Validated
public class PracticeQueryController {


    private final PracticeQueryService practiceQueryService;


    /**
     * 实践中心查看需求列表接口
     * 角色: 发布者
     *
     * @param myDemandQueryDTO 查询请求体
     * @return 需求列表
     */
    @PostMapping("/my-published-demand/list")
    public Result getMyPublishedDemandList(
            @Valid @RequestBody MyDemandQueryDTO myDemandQueryDTO) {
        return Result.success(practiceQueryService.getMyPublishedDemandList(SecurityUtils.getCurrentUserId(), myDemandQueryDTO));
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
        return Result.success(practiceQueryService.getApplyMemberList(SecurityUtils.getCurrentUserId(), demandId));
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
        return Result.success(practiceQueryService.getMyApplyDetail(applyId));
    }


    /**
     * 实践中心查看需求详情接口(复用)
     * 角色: 发布者
     *
     * @param demandId 需求id
     * @return 需求详情
     */
    @GetMapping("/demand/detail")
    public Result getPracticeDemandDetail(
            @NotNull Long demandId) {
        return Result.success(practiceQueryService.getPracticeDemandDetail(demandId));
    }


    /**
     * 查看我参与的实践需求列表接口
     * 角色: 认领者
     *
     * @param practiceDemandQueryDTO 分页查询请求体
     * @return 我参与的需求列表
     */
    @PostMapping("/my-joined-demand/list")
    public Result getMyJoinedPracticeList(
            @Valid @RequestBody PracticeDemandQueryDTO practiceDemandQueryDTO) {
        return Result.success(practiceQueryService.getMyJoinedPracticeList(SecurityUtils.getCurrentUserId(),practiceDemandQueryDTO));
    }


    /**
     * 查询实践需求的附件列表接口
     * 角色: 发布者 认领者
     *
     * @param demandId 需求id
     * @return 需求附件列表
     */
    @GetMapping("/progress-attachment/list")
    public Result getProgressAttachmentList(
            @NotNull @RequestParam Long demandId) {
        return Result.success(practiceQueryService.getProgressAttachmentList(demandId));
    }


    /**
     * 查询成员列表接口
     * 角色: 发布者 认领者
     *
     * @param demandId 需求id
     * @return 成员列表
     */
    @GetMapping("/member/list")
    public Result getMemberList(
            @NotNull @RequestParam Long demandId) {
        return Result.success(practiceQueryService.getMemberList(demandId));
    }


    /**
     * 查询需求成员变更记录接口
     * 角色: 发布者
     *
     * @param memberChangeLogDTO 分页查询请求体
     * @return 变更记录
     */
    @PostMapping("/member/change/log")
    public Result getDemandMemberChangeLog(
            @Valid @RequestBody MemberChangeLogDTO memberChangeLogDTO) {
        return Result.success(practiceQueryService.getDemandMemberChangeLog(memberChangeLogDTO));
    }


    /**
     * 查询需求日志接口
     * 角色: 发布者 认领者
     *
     * @param queryDemandProgressLogDTO  需求日志查询请求体
     * @return 需求日志列表
     */
    @GetMapping("/demand-progress/log")
    public Result getDemandProgressLog(
            @Valid @RequestBody QueryDemandProgressLogDTO queryDemandProgressLogDTO) {
        return Result.success(practiceQueryService.getDemandProgressLog(queryDemandProgressLogDTO));
    }
}