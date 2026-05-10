package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.LoginDTO;
import com.sc.qisi_system.module.user.dto.LogoutDTO;
import com.sc.qisi_system.module.user.vo.LoginUserVO;


public interface LoginService {


    LoginUserVO login(LoginDTO loginDTO);


    void logout(LogoutDTO logoutDTO);
}
