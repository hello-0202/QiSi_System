package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterDTO;


/**
 * 白名单校验业务服务接口
 * 处理学生、教师注册前的白名单校验相关操作
 */
public interface WhitelistCheckService {


    /**
     * 校验用户是否在注册白名单中
     *
     * @param request 学生/教师注册请求参数
     * @return 在白名单中返回true，不在返回false
     */
    boolean isInWhitelist(StudentTeacherRegisterDTO request);
}