package com.sc.qisi_system.module.user.controller;


import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.LoginRequest;
import com.sc.qisi_system.module.user.dto.LogoutRequest;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.service.impl.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class AuthController {

    private final LoginServiceImpl loginServiceimpl;
    private final RedisService redisService;


    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        return loginServiceimpl.login(loginRequest);
    }

    /**
     * 用户登出
     */
    @PostMapping("logout")
    public Result logout(@RequestBody LogoutRequest logoutRequest) {
        return loginServiceimpl.logout(logoutRequest);
    }

    /**
     * 刷新token
     */
    @PostMapping("/refresh-accessToken")
    public Result refreshAccessToken(@RequestParam String refreshToken) {
        return Result.success(redisService.refreshAccessToken(refreshToken));
    }


}
