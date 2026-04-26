package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.practice.dto.MemberChangeLogDTO;
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
    @PostMapping("/demand/list")
    public Result getPracticeDemandList(
            @Valid @RequestBody MyDemandQueryDTO myDemandQueryDTO) {
        return Result.success(practiceQueryService.getPracticeDemandList(SecurityUtils.getCurrentUserId(), myDemandQueryDTO));
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
     * 查看我参与的实践需求列表接口(复用)
     * 角色: 认领者
     *
     * @param practiceDemandQueryDTO 分页查询请求体
     * @return 我参与的需求列表
     */
    @PostMapping("/my-practice/list")
    public Result getMyPracticeList(
            @Valid @RequestBody PracticeDemandQueryDTO practiceDemandQueryDTO) {
        return Result.success(practiceQueryService.getMyPracticeList(SecurityUtils.getCurrentUserId(),practiceDemandQueryDTO));
    }


    /**
     * 查看实践需求详情接口
     * 角色: 认领者
     *
     * @param demandId 需求id
     * @return 需求详情
     */
    @GetMapping("/my-practice/detail")
    public Result getMyPracticeDetail(
            @NotNull @RequestParam Long demandId) {
        return Result.success(practiceQueryService.getMyPracticeDetail(demandId));
    }


    /**
     * 查询成员列表接口(复用)
     * 角色: 发布者 认领者
     *
     * @param demandId 需求id
     * @return 成员列表
     */
    @GetMapping("/member/list")
    public Result getMemberList(
            @NotNull @RequestParam Long demandId) {
        return Result.success(practiceQueryService.getMemberList(SecurityUtils.getCurrentUserId(), demandId));
    }


    /**
     * 查询成员详细信息接口(复用)
     * 角色: 发布者 认领者
     *
     * @param userId 用户id
     * @return 用户详细信息
     */
    @GetMapping("/member/info")
    public Result getMemberDetailInfo(
            @NotNull @RequestParam Long userId) {
        return Result.success(practiceQueryService.getMemberDetailInfo(userId));
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
}