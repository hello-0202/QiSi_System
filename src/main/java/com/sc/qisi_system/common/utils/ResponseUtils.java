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

    public void writeResult(HttpServletResponse response, Result result) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(result));
    }
}
