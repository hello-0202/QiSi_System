package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.vo.UserInfoVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;

public interface UserInfoService {


    UserInfoVO getUserInfo();


    UserProfileVO getUserProfile();
}
