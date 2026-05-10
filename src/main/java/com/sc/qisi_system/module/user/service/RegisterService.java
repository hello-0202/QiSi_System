package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.module.user.dto.EnterpriseRegisterDTO;
import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterDTO;

public interface RegisterService {

    void registerStudentTeacher (StudentTeacherRegisterDTO request);

    void registerEnterprise(EnterpriseRegisterDTO request);

}
