package com.sc.qisi_system.module.apply.controller;

import com.sc.qisi_system.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 需求认领申请
 */

@RequestMapping("/api/demand/apply")
@RequiredArgsConstructor
@RestController
public class DemandApplyController {


    //TODO
    /**
     * 提交需求认领申请接口
     */
    @PostMapping("/submit")
    public Result submitApply() {
        return null;
    }


    //TODO
    /**
     * 分页查询我的认领申请列表接口
     */
    @GetMapping("/my-list")
    public Result getMyApplyList() {
        return null;
    }


    //TODO
    /**
     * 取消认领申请接口（仅申请中可取消）
     */
    @DeleteMapping("/cancel")
    public Result cancelApply() {
        return null;
    }


}
