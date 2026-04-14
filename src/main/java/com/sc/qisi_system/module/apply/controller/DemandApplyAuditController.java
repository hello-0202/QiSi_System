package com.sc.qisi_system.module.apply.controller;

import com.sc.qisi_system.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 需求认领审核
 */
@RequestMapping("/api/demand/apply/audit")
@RequiredArgsConstructor
@RestController
public class DemandApplyAuditController {


    //TODO
    /**
     * 查看指定需求的申请成员列表
     */
    @GetMapping("/demand/member-list/")
    public Result getApplyMemberList() {
        return null;
    }


    //TODO
    /**
     * 选择/通过申请成员
     */
    @PostMapping("/select")
    public Result selectApplyMember() {
        return null;
    }

}
