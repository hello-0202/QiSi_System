package com.sc.qisi_system.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.qisi_system.common.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseUtils {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // 原有：默认http 200
    public void writeResult(HttpServletResponse response, Result result) throws IOException {
        writeResult(response, result, HttpServletResponse.SC_OK);
    }

    // ✅新增重载，可以指定HTTP状态码
    public void writeResult(HttpServletResponse response, Result result, int httpStatus) throws IOException {
        response.setStatus(httpStatus); // 设置HTTP状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(result));
    }
}