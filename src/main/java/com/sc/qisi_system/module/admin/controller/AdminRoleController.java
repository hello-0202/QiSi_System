package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/role")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminRoleController {


    //TODO
    /**
     * 将用户【设置为需求发布者】
     * 学生 → 发布者
     */
    @PostMapping("/set-publisher")
    public Result setUserToPublisher() {
        // 业务逻辑：修改用户角色（学生 → 发布者）
        return Result.success("设置为发布者成功");
    }


    //TODO
    /**
     * 取消用户【需求发布者权限】
     * 发布者 → 恢复为普通角色
     */
    @PostMapping("/cancel-publisher")
    public Result cancelUserPublisher() {
        // 业务逻辑：撤销发布者角色
        return Result.success("取消发布者权限成功");
    }
}