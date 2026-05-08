package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.LoginRequest;
import com.sc.qisi_system.module.user.dto.LogoutRequest;
import com.sc.qisi_system.module.user.vo.LoginUserVO;


public interface LoginService {


    LoginUserVO login(LoginRequest loginRequest);


    void logout(LogoutRequest logoutRequest);
}
