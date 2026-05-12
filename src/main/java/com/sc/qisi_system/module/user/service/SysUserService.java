package com.sc.qisi_system.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import com.sc.qisi_system.module.user.entity.SysUser;


/**
 * 系统用户业务服务接口
 * 处理用户信息查询、用户状态校验、用户资料获取等相关操作
 */
public interface SysUserService extends IService<SysUser> {


    /**
     * 根据用户ID判断用户是否存在
     *
     * @param userId 用户ID
     * @return 存在返回true，不存在返回false
     */
    boolean existsById(Long userId);


    /**
     * 获取用户个人资料信息
     *
     * @param userId 用户ID
     * @return 用户个人资料视图对象
     */
    UserProfileVO getUserProfile(Long userId);
}