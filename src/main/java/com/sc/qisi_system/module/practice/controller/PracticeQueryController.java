package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.utils.SecurityUtils;
import com.sc.qisi_system.module.demand.dto.MyDemandQueryDTO;
import com.sc.qisi_system.module.demand.dto.PracticeDemandQueryDTO;
import com.sc.qisi_system.module.practice.service.PracticeQueryService;
import jakarta.validation.constraints.NotBlank;
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
     * @return 统一返回结果
     */
    @GetMapping("/demand/list")
    public Result getPracticeDemandList(
            @NotBlank @RequestBody MyDemandQueryDTO myDemandQueryDTO) {
        return Result.success(practiceQueryService.getPracticeDemandList(SecurityUtils.getCurrentUserId(), myDemandQueryDTO));
    }


    /**
     * 实践中心查看需求详情接口(复用)
     * 角色: 发布者
     *
     * @return 统一返回结果
     */
    @GetMapping("/demand/detail")
    public Result getPracticeDemandDetail(
            @NotNull Long demandId) {
        return Result.success(practiceQueryService.getPracticeDemandDetail(demandId));
    }


    /**
     * 查看我参与的实践需求列表(复用)
     * 角色: 认领者
     */
    @GetMapping("/my-practice/list")
    public Result getMyPracticeList(
            @RequestBody PracticeDemandQueryDTO practiceDemandQueryDTO) {
        return Result.success(practiceQueryService.getMyPracticeList(SecurityUtils.getCurrentUserId(),practiceDemandQueryDTO));
    }


    /**
     * 查看实践需求详情(复用)
     * 角色: 认领者
     */
    @GetMapping("/my-practice/detail")
    public Result getMyPracticeDetail(
            @NotNull @RequestParam Long demandId) {
        return Result.success(practiceQueryService.getMyPracticeDetail(demandId));
    }


    /**
     * 查询成员列表接口(复用)
     *
     * @return 统一返回结果
     */
    @GetMapping("/member/list")
    public Result getMemberList(
            @NotNull @RequestParam Long demandId) {
        return Result.success(practiceQueryService.getMemberList(SecurityUtils.getCurrentUserId(), demandId));
    }


    /**
     * 查询成员详细信息接口(复用)
     *
     * @return 统一返回结果
     */
    @GetMapping("/member/info")
    public Result getMemberDetailInfo(
            @NotNull @RequestParam Long userId) {
        return Result.success(practiceQueryService.getMemberDetailInfo(userId));
    }


    //TODO
    /**
     * 查询需求成员变更记录接口
     *
     * @return 统一返回结果
     */
    @GetMapping("/member/change/log")
    public Result getDemandMemberChangeLog() {
        return null;
    }
}