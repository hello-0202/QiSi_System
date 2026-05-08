package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.UserInfoDTO;
import com.sc.qisi_system.module.user.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户信息操作接口
 */
@RequestMapping("/api/user/info")
@RequiredArgsConstructor
@RestController
@Validated
public class UserInfoController {


    private final UserInfoService userInfoService;


    /**
     * 查询当前登录用户
     */
    @GetMapping("//base")
    public Result getUserInfo() {
        return Result.success(userInfoService.getUserInfo());
    }


    /**
     * 查询当前登录用户扩展信息
     */
    @GetMapping("/profile")
    public Result getProfile() {
        return Result.success(userInfoService.getUserProfile());
    }


    /**
     * 修改用户信息接口 密码 手机号 邮箱
     *
     * @return 统一返回结果
     */
    @PutMapping("/update-info")
    public Result updateUserInfo(
            @Valid @RequestBody UserInfoDTO userInfoDTO) {
        userInfoService.updateUserInfo(userInfoDTO);
        return Result.success();
    }


    /**
     * 修改头像接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/avatar")
    public Result updateAvatar(
            @RequestParam("file") MultipartFile file) {
        userInfoService.updateAvatar(file);
        return Result.success();
    }
}