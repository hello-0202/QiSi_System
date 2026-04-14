package com.sc.qisi_system.module.user.controller;


import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.LoginRequest;
import com.sc.qisi_system.module.user.dto.LogoutRequest;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.service.impl.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final LoginServiceImpl loginServiceimpl;
    private final RedisService redisService;


    /**
     * 用户登录接口
     * @param loginRequest 登录请求参数
     * @return 成功返回相关参数，失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        return loginServiceimpl.login(loginRequest);
    }

    /**
     * 用户登出接口
     * @param logoutRequest 登出请求参数
     * @return 成功返回统一结果，失败返回错误信息
     */
    @PostMapping("logout")
    public Result logout(@RequestBody LogoutRequest logoutRequest) {
        return loginServiceimpl.logout(logoutRequest);
    }

    /**
     * 刷新token接口
     * @param refreshToken 刷新token
     * @return 成功返回accessToken和refreshToken，失败返回错误信息
     */
    @PostMapping("/refresh-accessToken")
    public Result refreshAccessToken(@RequestParam String refreshToken) {
        return Result.success(redisService.refreshAccessToken(refreshToken));
    }


}
