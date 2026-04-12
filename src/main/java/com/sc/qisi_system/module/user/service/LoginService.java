package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.LoginRequest;
import com.sc.qisi_system.module.user.dto.LogoutRequest;

public interface LoginService {

    Result login(LoginRequest loginRequest);

    Result logout(LogoutRequest logoutRequest);


}
