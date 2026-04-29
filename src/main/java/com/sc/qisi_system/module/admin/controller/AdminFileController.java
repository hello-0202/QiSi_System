package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/file")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminFileController {

    /**
     * 导入教职工白名单（Excel）
     */
    @PostMapping("/import-teacher-whitelist")
    public Result importTeacherWhitelist() {
        // 待实现
        return Result.success("导入成功");
    }

    /**
     * 导入学生白名单（Excel）
     */
    @PostMapping("/import-student-whitelist")
    public Result importStudentWhitelist() {
        // 待实现
        return Result.success("导入成功");
    }

}