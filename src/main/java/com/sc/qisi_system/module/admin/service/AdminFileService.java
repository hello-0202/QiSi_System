package com.sc.qisi_system.module.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminFileService {


    void importTeacherWhitelist(MultipartFile file) throws IOException;


    void importStudentWhitelist(MultipartFile file) throws IOException;
}
