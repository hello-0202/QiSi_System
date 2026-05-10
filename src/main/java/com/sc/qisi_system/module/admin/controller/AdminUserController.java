package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.dto.SysUserQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminUserService;
import com.sc.qisi_system.module.user.dto.SysUserResetPasswordDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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


    private final AdminUserService adminUserService;


    /**
     * 条件查询用户列表
     */
    @GetMapping("/list")
    public Result getUserList(
            @RequestBody SysUserQueryDTO sysUserQueryDTO) {
        return Result.success(adminUserService.getUserList(sysUserQueryDTO));
    }


    /**
     * 管理员重置用户密码
     */
    @PostMapping("/reset-password")
    public Result resetUserPassword(
            @Valid @RequestBody SysUserResetPasswordDTO dto) {
        adminUserService.resetUserPassword(dto);
        return Result.success("密码重置成功");
    }


    /**
     * 封禁用户接口
     */
    @PostMapping("/ban")
    public Result banUser(
            @NotNull @RequestParam Long userId) {
        adminUserService.banUser(userId);
        return Result.success();
    }


    /**
     * 解除用户封禁
     */
    @PostMapping("/unban")
    public Result unbanUser(
            @NotNull @RequestParam Long userId) {
        adminUserService.unbanUser(userId);
        return Result.success();
    }
}