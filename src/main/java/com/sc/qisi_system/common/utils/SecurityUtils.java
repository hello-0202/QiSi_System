package com.sc.qisi_system.common.utils;

import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.config.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    /**
     * 私有构造，禁止实例化
     */
    private SecurityUtils() {}

    /**
     * 获取当前认证信息
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户
     */
    public static LoginUser getLoginUser() {
        Authentication auth = getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            throw new BusinessException(ResultCode.USER_NOT_LOGIN);
        }
        return (LoginUser) auth.getPrincipal();
    }

    /**
     * 获取当前登录用户ID
     */
    public static Long getCurrentUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取当前登录用户名
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 判断是否为管理员
     */
    public static boolean isAdmin() {
        return getLoginUser().getAuthorities().stream()
                .anyMatch(auth -> UserTypeEnum.ADMIN.getAuthority().equals(auth.getAuthority()));
    }

    /**
     * 判断是否为学生
     */
    public static boolean isStudent() {
        return getLoginUser().getAuthorities().stream()
                .anyMatch(auth -> UserTypeEnum.STUDENT.getAuthority().equals(auth.getAuthority()));
    }

    /**
     * 清除安全上下文（请求结束时调用，防止内存泄漏）
     */
    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
