package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.LoginDTO;
import com.sc.qisi_system.module.user.dto.LogoutDTO;
import com.sc.qisi_system.module.user.vo.LoginUserVO;


/**
 * 登录认证业务服务接口
 */
public interface LoginService {


    /**
     * 用户登录接口
     *
     * @param loginDTO 登录请求参数
     * @return 登录成功返回用户信息与令牌
     */
    LoginUserVO login(LoginDTO loginDTO);


    /**
     * 用户退出登录接口
     *
     * @param logoutDTO 退出登录参数
     */
    void logout(LogoutDTO logoutDTO);
}