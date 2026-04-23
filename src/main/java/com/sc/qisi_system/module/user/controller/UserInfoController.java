package com.sc.qisi_system.module.user.controller;

import com.sc.qisi_system.common.result.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;


/**
 * 用户信息操作接口
 */
@RequestMapping("/api/user/info")
@RequiredArgsConstructor
@RestController
@Validated
public class UserInfoController {


    //TODO
    /**
     * 修改密码接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/password")
    public Result updatePassword() {
        return null;
    }


    //TODO
    /**
     * 修改头像接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/avatar")
    public Result updateAvatar() {
        return null;
    }


    //TODO
    /**
     * 修改手机号接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/phone")
    public Result updatePhone() {
        return null;
    }


    //TODO
    /**
     * 修改邮箱接口
     *
     * @return 统一返回结果
     */
    @PutMapping("/email")
    public Result updateEmail() {
        return null;
    }
}