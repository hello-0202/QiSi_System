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

@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/whitelist")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminWhitelistController {


    private final AdminWhitelistService adminWhitelistService;


    /**
     * 条件查询教职工白名单
     */
    @GetMapping("/teacher-whitelist/list")
    public Result getTeacherWhitelist(
            @RequestBody SchoolStaffWhitelistQueryDTO queryDTO) {
        return Result.success(adminWhitelistService.getTeacherWhitelist(queryDTO));
    }


    /**
     * 修改教职工白名单
     */
    @PutMapping("/teacher-whitelist/update")
    public Result updateTeacherWhitelist(
            @RequestBody SchoolStaffDTO schoolStaffDTO) {
        adminWhitelistService.updateTeacherWhitelist(schoolStaffDTO);
        return Result.success();
    }


    /**
     * 删除教职工白名单
     */
    @DeleteMapping("/teacher-whitelist/delete")
    public Result deleteTeacherWhitelist(
            @NotNull @RequestParam Long id) {
        adminWhitelistService.deleteTeacherWhitelist(id);
        return Result.success();
    }


    /**
     * 新增教职工白名单
     */
    @PostMapping("/teacher-whitelist/add")
    public Result addTeacherWhitelist(
            @RequestBody SchoolStaffDTO schoolStaffDTO) {
        adminWhitelistService.addTeacherWhitelist(schoolStaffDTO);
        return Result.success();
    }


    /**
     * 条件查询学生白名单接口
     *
     * @param schoolStudentWhitelistQueryDTO 查询请求体
     * @return 学生白名单列表
     */
    @GetMapping("/student-whitelist/list")
    public Result getStudentWhitelist(
            @RequestBody SchoolStudentWhitelistQueryDTO schoolStudentWhitelistQueryDTO) {
        return Result.success(adminWhitelistService.getStudentWhitelist(schoolStudentWhitelistQueryDTO));
    }


    /**
     * 修改学生白名单
     */
    @PutMapping("/student-whitelist/update")
    public Result updateStudentWhitelist(
            @Valid @RequestBody SchoolStudentDTO studentWhitelistDTO) {
        adminWhitelistService.updateStudentWhitelist(studentWhitelistDTO);
        return Result.success();
    }


    /**
     * 删除学生白名单
     */
    @DeleteMapping("/student-whitelist/delete")
    public Result deleteStudentWhitelist(
            @NotNull @RequestParam Long id) {
        adminWhitelistService.deleteStudentWhitelist(id);
        return Result.success();
    }


    /**
     * 白名单新增学生
     */
    @PostMapping("/student-whitelist/add")
    public Result addStudentWhitelist(
            @Valid @RequestBody SchoolStudentDTO studentWhitelistDTO) {
        adminWhitelistService.addStudentWhitelist(studentWhitelistDTO);
        return Result.success();
    }
}
