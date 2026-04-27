package com.sc.qisi_system.module.practice.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.practice.dto.DemandProgressDTO;
import com.sc.qisi_system.module.practice.service.PracticeExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
     * @return 统一返回结果
     */
    @PostMapping("/log")
    public Result submitDemandLog(DemandProgressDTO demandProgressDTO) {
        practiceExecuteService.submitDemandPlan(demandProgressDTO);
        return Result.success();
    }


    //TODO
    /**
     * 提交需求附件接口
     *
     * @return 统一返回结果
     */
    @PostMapping("/attachment")
    public Result submitDemandAttachment() {
        return null;
    }


    //TODO
    /**
     * 更新需求计划接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/plan")
    public Result updateDemandPlan() {
        return null;
    }


    //TODO
    /**
     * 申请退出需求接口
     *
     * @return 统一返回结果
     */
    @PostMapping("/quit")
    public Result applyQuitDemand() {
        return null;
    }
}