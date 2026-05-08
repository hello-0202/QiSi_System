package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminUserController {


    //TODO
    /**
     * 条件查询用户列表
     */
    @GetMapping("/list")
    public Result getUserList() {
        return Result.success(null);
    }


    //TODO
    /**
     * 查询用户详情
     */
    @GetMapping("/detail")
    public Result getUserDetail() {
        return Result.success(null);
    }


    //TODO
    /**
     * 封禁用户接口
     */
    @PostMapping("/ban")
    public Result banUser() {
        return Result.success();
    }


    //TODO
    /**
     * 解除用户封禁
     */
    @PostMapping("/unban")
    public Result unbanUser() {
        return Result.success();
    }
}