package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.LoginRequest;
import com.sc.qisi_system.module.user.dto.LogoutRequest;
import com.sc.qisi_system.module.user.service.LoginService;
import com.sc.qisi_system.module.user.service.RedisService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 用户认证控制器
 */
@Slf4j
@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class AuthController {


    private final LoginService loginService;
    private final RedisService redisService;


    /**
     * 用户登录接口
     * @param loginRequest 登录请求参数
     * @return 成功返回相关参数，失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        return Result.success(loginService.login(loginRequest));
    }


    /**
     * 用户登出接口
     * @param logoutRequest 登出请求参数
     * @return 成功返回统一结果，失败返回错误信息
     */
    @PostMapping("logout")
    public Result logout(@RequestBody LogoutRequest logoutRequest) {
        loginService.logout(logoutRequest);
        return Result.success();
    }


    /**
     * 刷新token接口
     * @param refreshToken 刷新token
     * @return 成功返回accessToken和refreshToken，失败返回错误信息
     */
    @PostMapping("/refresh-accessToken")
    public Result refreshAccessToken(@NotBlank @RequestParam String refreshToken) {
        return Result.success(redisService.refreshAccessToken(refreshToken));
    }


}
