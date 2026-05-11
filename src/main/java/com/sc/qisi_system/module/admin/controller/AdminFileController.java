package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.service.AdminFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 文件导入控制器
 * 功能: 教职工白名单、学生白名单Excel批量导入等文件相关管理操作
 */
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/file")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminFileController {


    private final AdminFileService adminFileService;


    /**
     * 导入教职工白名单接口
     * 角色: 管理员
     *
     * @param file 上传的Excel文件
     * @return 统一返回结果
     * @throws IOException 文件读取异常
     */
    @PostMapping("/import-teacher-whitelist")
    @PreAuthorize("hasRole('ADMIN')")
    public Result importTeacherWhitelist(
            @RequestParam("file") MultipartFile file) throws IOException {
        adminFileService.importTeacherWhitelist(file);
        return Result.success();
    }


    /**
     * 导入学生白名单接口
     * 角色: 管理员
     *
     * @param file 上传的Excel文件
     * @return 导入成功提示结果
     * @throws IOException 文件读取异常
     */
    @PostMapping("/import-student-whitelist")
    @PreAuthorize("hasRole('ADMIN')")
    public Result importStudentWhitelist(
            @RequestParam("file") MultipartFile file) throws IOException {
        adminFileService.importStudentWhitelist(file);
        return Result.success("导入成功");
    }

}