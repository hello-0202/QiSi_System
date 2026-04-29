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
     * 条件查询教职工白名单
     */
    @GetMapping("/teacher-whitelist/list")
    public Result getTeacherWhitelist() {
        return Result.success(null);
    }


    //TODO
    /**
     * 修改教职工白名单
     */
    @PutMapping("/teacher-whitelist/update")
    public Result updateTeacherWhitelist() {
        return Result.success("修改成功");
    }


    //TODO
    /**
     * 删除教职工白名单
     */
    @DeleteMapping("/teacher-whitelist/delete")
    public Result deleteTeacherWhitelist() {
        return Result.success("删除成功");
    }


    //TODO
    /**
     * 新增教职工白名单
     */
    @PostMapping("/teacher-whitelist/add")
    public Result addTeacherWhitelist() {
        return Result.success("新增成功");
    }


    //TODO
    /**
     * 条件查询学生白名单
     */
    @GetMapping("/student-whitelist/list")
    public Result getStudentWhitelist() {
        return Result.success(null);
    }


    //TODO
    /**
     * 修改学生白名单
     */
    @PutMapping("/student-whitelist/update")
    public Result updateStudentWhitelist() {
        return Result.success("修改成功");
    }


    //TODO
    /**
     * 删除学生白名单
     */
    @DeleteMapping("/student-whitelist/delete")
    public Result deleteStudentWhitelist() {
        return Result.success("删除成功");
    }


    //TODO
    /**
     * 新增学生白名单
     */
    @PostMapping("/student-whitelist/add")
    public Result addStudentWhitelist() {
        return Result.success("新增成功");
    }


    //TODO
    /**
     * 封禁用户接口
     */
    @PostMapping("/ban")
    public Result banUser() {
        return Result.success("操作成功");
    }


    //TODO
    /**
     * 解除用户封禁
     */
    @PostMapping("/unban")
    public Result unbanUser() {
        return Result.success("用户解封成功");
    }
}