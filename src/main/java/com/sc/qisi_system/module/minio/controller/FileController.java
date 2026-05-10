package com.sc.qisi_system.module.minio.controller;

import com.sc.qisi_system.module.minio.service.MinioService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * 文件操作控制器
 */
@RequiredArgsConstructor
@RequestMapping("/file")
@RestController
public class FileController {


    private final MinioService minioService;


    /**
     * 文件下载接口
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件对象名称
     * @param response 响应对象
     * @throws IOException IO异常
     */
    @GetMapping("/download")
    public void download(
            @RequestParam String bucketName,
            @RequestParam String objectName,
            HttpServletResponse response) throws IOException {
        try (InputStream is = minioService.downloadFile(bucketName, objectName)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectName, StandardCharsets.UTF_8));
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}