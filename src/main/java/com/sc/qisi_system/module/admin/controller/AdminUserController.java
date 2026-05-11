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


/**
 * 用户管理控制器
 * 功能: 用户列表查询、密码重置、用户封禁/解封等用户管理操作
 */
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminUserController {


    private final AdminUserService adminUserService;


    /**
     * 条件查询用户列表接口
     * 角色: 管理员
     *
     * @param sysUserQueryDTO 用户查询条件
     * @return 用户分页列表
     */
    @PostMapping("/list")
    public Result getUserList(
            @RequestBody SysUserQueryDTO sysUserQueryDTO) {
        return Result.success(adminUserService.getUserList(sysUserQueryDTO));
    }


    /**
     * 管理员重置用户密码接口
     * 角色: 管理员
     *
     * @param dto 密码重置参数
     * @return 密码重置成功提示
     */
    @PostMapping("/reset-password")
    public Result resetUserPassword(
            @Valid @RequestBody SysUserResetPasswordDTO dto) {
        adminUserService.resetUserPassword(dto);
        return Result.success("密码重置成功");
    }


    /**
     * 封禁用户接口
     * 角色: 管理员
     *
     * @param userId 用户ID
     * @return 统一返回结果
     */
    @GetMapping("/ban")
    public Result banUser(
            @NotNull @RequestParam Long userId) {
        adminUserService.banUser(userId);
        return Result.success();
    }


    /**
     * 解除用户封禁接口
     * 角色: 管理员
     *
     * @param userId 用户ID
     * @return 统一返回结果
     */
    @GetMapping("/unban")
    public Result unbanUser(
            @NotNull @RequestParam Long userId) {
        adminUserService.unbanUser(userId);
        return Result.success();
    }
}