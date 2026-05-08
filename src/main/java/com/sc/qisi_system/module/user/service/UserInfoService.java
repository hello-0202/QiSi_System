package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.UserInfoDTO;
import com.sc.qisi_system.module.user.vo.UserInfoVO;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {


    UserInfoVO getUserInfo();


    UserProfileVO getUserProfile();


    void updateUserInfo(UserInfoDTO userInfoDTO);


    void updateAvatar(MultipartFile file);
}
