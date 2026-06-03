package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.common.enums.UserIdentityEnum;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.MenuQueryDTO;
import com.sc.qisi_system.module.admin.vo.MenuRouteVO;
import com.sc.qisi_system.module.user.dto.UserInfoDTO;
import com.sc.qisi_system.module.user.vo.EntEmployeeInfoVO;
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
     * 修改用户密码
     *
     * @param password 用户信息请求体
     */
    void updatePassword(String password);


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


    /**
     * 更新企业人员扩展信息
     *
     * @param entEmployeeInfoVO 请求体
     */
    void updateEntEmployeeInfo(EntEmployeeInfoVO entEmployeeInfoVO);

    UserIdentityEnum getUserIdentity();

    PageResult<MenuRouteVO> getRouters(MenuQueryDTO menuQueryDTO);
}