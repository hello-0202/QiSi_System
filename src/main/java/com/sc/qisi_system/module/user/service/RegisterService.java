package com.sc.qisi_system.module.user.service;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.user.dto.EnterpriseRegisterRequest;
import com.sc.qisi_system.module.user.dto.StudentTeacherRegisterRequest;

public interface RegisterService {

    Result registerStudentTeacher (StudentTeacherRegisterRequest request);

    Result registerEnterprise(EnterpriseRegisterRequest request);

}
