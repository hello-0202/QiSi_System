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
 * 用户信息操作控制器
 */
@RequestMapping("/api/user/info")
@RequiredArgsConstructor
@RestController
@Validated
public class UserInfoController {


    private final UserInfoService userInfoService;


    /**
     * 获取当前登录用户基础信息接口
     *
     * @return 用户基础信息
     */
    @GetMapping("/base")
    public Result getUserInfo() {
        return Result.success(userInfoService.getUserInfo());
    }


    /**
     * 获取当前登录用户详细资料接口
     *
     * @return 用户详细资料
     */
    @GetMapping("/profile")
    public Result getProfile() {
        return Result.success(userInfoService.getUserProfile());
    }


    /**
     * 修改用户基础信息接口
     *
     * @param userInfoDTO 用户信息
     * @return 成功返回相关信息，失败返回错误信息
     */
    @PutMapping("/update-info")
    public Result updateUserInfo(
            @Valid @RequestBody UserInfoDTO userInfoDTO) {
        userInfoService.updateUserInfo(userInfoDTO);
        return Result.success();
    }


    /**
     * 修改用户头像接口
     *
     * @param file 头像文件
     * @return 成功返回相关信息，失败返回错误信息
     */
    @PutMapping("/avatar")
    public Result updateAvatar(
            @RequestParam("file") MultipartFile file) {
        userInfoService.updateAvatar(file);
        return Result.success();
    }
}