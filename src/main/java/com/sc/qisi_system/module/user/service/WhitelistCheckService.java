package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterDTO;

public interface WhitelistCheckService {

    boolean isInWhitelist(StudentTeacherRegisterDTO request);
}
