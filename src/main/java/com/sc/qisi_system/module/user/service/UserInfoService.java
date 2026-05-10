package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.UserInfoDTO;
import com.sc.qisi_system.module.user.vo.UserInfoVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户信息业务服务接口
 * 提供用户信息查询、资料修改、头像更新等操作
 */
public interface UserInfoService {


    /**
     * 获取当前登录用户基础信息
     *
     * @return 用户基础信息
     */
    UserInfoVO getUserInfo();


    /**
     * 获取当前登录用户扩展资料信息
     *
     * @return 用户扩展资料
     */
    UserProfileVO getUserProfile();


    /**
     * 修改用户基础信息
     *
     * @param userInfoDTO 用户信息参数
     */
    void updateUserInfo(UserInfoDTO userInfoDTO);


    /**
     * 修改用户头像
     *
     * @param file 头像文件
     */
    void updateAvatar(MultipartFile file);
}