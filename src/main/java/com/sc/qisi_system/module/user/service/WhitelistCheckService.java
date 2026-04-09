package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterRequest;

public interface WhitelistCheckService {

    boolean isInWhitelist(StudentTeacherRegisterRequest request);
}
