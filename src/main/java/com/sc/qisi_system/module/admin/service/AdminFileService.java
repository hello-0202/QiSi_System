package com.sc.qisi_system.module.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 管理员文件服务接口
 * 功能: 教职工白名单、学生白名单Excel文件导入业务处理
 */
public interface AdminFileService {


    /**
     * 导入教职工白名单
     * 角色: 管理员
     *
     * @param file 上传的Excel文件
     * @throws IOException 文件读取异常
     */
    void importTeacherWhitelist(MultipartFile file) throws IOException;


    /**
     * 导入学生白名单
     * 角色: 管理员
     *
     * @param file 上传的Excel文件
     * @throws IOException 文件读取异常
     */
    void importStudentWhitelist(MultipartFile file) throws IOException;
}