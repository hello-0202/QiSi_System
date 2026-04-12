package com.sc.qisi_system.config.security.handler;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.RequestUtils;
import com.sc.qisi_system.common.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityHandlerConfig {

    private final ResponseUtils responseUtils;
    private final RequestUtils requestUtils;

    /**
     * 自定义认证失败处理器
     */
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            log.warn("[认证失败] {}, 原因: {}",requestUtils.getRequestLog(request),  authException.getMessage());
            Result result = Result.error(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
            responseUtils.writeResult(response, result);
        };
    }

    /**
     * 自定义授权失败处理器
     */
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            log.error("[授权失败] {}, 用户: {}, 原因: {}",requestUtils.getRequestLog(request),
                    request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "unknown",
                    accessDeniedException.getMessage());
            Result result = Result.error(ResultCode.PERMISSION_DENIED.getCode(), ResultCode.PERMISSION_DENIED.getMessage());
            responseUtils.writeResult(response, result);

        };
    }
}
