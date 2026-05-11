package com.sc.qisi_system.module.admin.controller;


import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.dto.SchoolStaffDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStaffWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminWhitelistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 白名单管理控制器
 * 功能: 教职工白名单、学生白名单的查询、新增、修改、删除等管理操作
 */
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/whitelist")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminWhitelistController {


    private final AdminWhitelistService adminWhitelistService;


    /**
     * 条件查询教职工白名单接口
     * 角色: 管理员
     *
     * @param queryDTO 教职工白名单查询条件
     * @return 教职工白名单分页列表
     */
    @PostMapping("/teacher-whitelist/list")
    public Result getTeacherWhitelist(
            @RequestBody SchoolStaffWhitelistQueryDTO queryDTO) {
        return Result.success(adminWhitelistService.getTeacherWhitelist(queryDTO));
    }


    /**
     * 修改教职工白名单接口
     * 角色: 管理员
     *
     * @param schoolStaffDTO 教职工信息
     * @return 统一返回结果
     */
    @PutMapping("/teacher-whitelist/update")
    public Result updateTeacherWhitelist(
            @RequestBody SchoolStaffDTO schoolStaffDTO) {
        adminWhitelistService.updateTeacherWhitelist(schoolStaffDTO);
        return Result.success();
    }


    /**
     * 删除教职工白名单接口
     * 角色: 管理员
     *
     * @param id 教职工白名单ID
     * @return 统一返回结果
     */
    @DeleteMapping("/teacher-whitelist/delete")
    public Result deleteTeacherWhitelist(
            @NotNull @RequestParam Long id) {
        adminWhitelistService.deleteTeacherWhitelist(id);
        return Result.success();
    }


    /**
     * 新增教职工白名单接口
     * 角色: 管理员
     *
     * @param schoolStaffDTO 教职工信息
     * @return 统一返回结果
     */
    @PostMapping("/teacher-whitelist/add")
    public Result addTeacherWhitelist(
            @RequestBody SchoolStaffDTO schoolStaffDTO) {
        adminWhitelistService.addTeacherWhitelist(schoolStaffDTO);
        return Result.success();
    }


    /**
     * 条件查询学生白名单接口
     * 角色: 管理员
     *
     * @param schoolStudentWhitelistQueryDTO 学生白名单查询条件
     * @return 学生白名单分页列表
     */
    @PostMapping("/student-whitelist/list")
    public Result getStudentWhitelist(
            @RequestBody SchoolStudentWhitelistQueryDTO schoolStudentWhitelistQueryDTO) {
        return Result.success(adminWhitelistService.getStudentWhitelist(schoolStudentWhitelistQueryDTO));
    }


    /**
     * 修改学生白名单接口
     * 角色: 管理员
     *
     * @param studentWhitelistDTO 学生信息
     * @return 统一返回结果
     */
    @PutMapping("/student-whitelist/update")
    public Result updateStudentWhitelist(
            @Valid @RequestBody SchoolStudentDTO studentWhitelistDTO) {
        adminWhitelistService.updateStudentWhitelist(studentWhitelistDTO);
        return Result.success();
    }


    /**
     * 删除学生白名单接口
     * 角色: 管理员
     *
     * @param id 学生白名单ID
     * @return 统一返回结果
     */
    @DeleteMapping("/student-whitelist/delete")
    public Result deleteStudentWhitelist(
            @NotNull @RequestParam Long id) {
        adminWhitelistService.deleteStudentWhitelist(id);
        return Result.success();
    }


    /**
     * 新增学生白名单接口
     * 角色: 管理员
     *
     * @param studentWhitelistDTO 学生信息
     * @return 统一返回结果
     */
    @PostMapping("/student-whitelist/add")
    public Result addStudentWhitelist(
            @Valid @RequestBody SchoolStudentDTO studentWhitelistDTO) {
        adminWhitelistService.addStudentWhitelist(studentWhitelistDTO);
        return Result.success();
    }
}