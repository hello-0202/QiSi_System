package com.sc.qisi_system.module.user.controller;


import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.LoginRequest;
import com.sc.qisi_system.module.user.service.impl.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class LoginController {

    private final LoginServiceImpl loginServiceimpl;


    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        return loginServiceimpl.login(loginRequest);
    }


}
