package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 实践需求审核操作控制器
 */
@RequestMapping("/api/practice/audit")
@RequiredArgsConstructor
@RestController
@Validated
public class PracticeAuditController {


    //TODO
    /**
     * 提交需求计划接口
     *
     * @return 统一返回结果
     */
    @PostMapping("/plan/submit")
    public Result submitDemandPlan() {
        return null;
    }


    //TODO
    /**
     * 开始研究接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/research/start")
    public Result startResearch() {
        return null;
    }


    //TODO
    /**
     * 完成研究接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/research/complete")
    public Result completeResearch() {
        return null;
    }


    //TODO
    /**
     * 关闭需求接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/demand/close")
    public Result closeDemand() {
        return null;
    }


    //TODO
    /**
     * 踢出成员接口
     *
     * @return 统一返回结果
     */
    @DeleteMapping("/member/kick")
    public Result kickMember() {
        return null;
    }


    //TODO
    /**
     * 发布者审核成员退出申请接口
     *
     * @return 统一返回结果
     */
    @PostMapping("/quit/audit")
    public Result auditMemberQuitApply() {
        return null;
    }
}