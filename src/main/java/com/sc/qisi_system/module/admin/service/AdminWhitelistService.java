package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.SchoolStaffDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStaffWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.vo.SchoolStaffVO;
import com.sc.qisi_system.module.admin.vo.SchoolStudentVO;

public interface AdminWhitelistService {


    PageResult<SchoolStaffVO> getTeacherWhitelist(SchoolStaffWhitelistQueryDTO queryDTO);


    void updateTeacherWhitelist(SchoolStaffDTO schoolStaffDTO);


    void deleteTeacherWhitelist(Long id);


    void addTeacherWhitelist(SchoolStaffDTO schoolStaffDTO);


    /**
     * 条件查询学生白名单接口
     *
     * @param queryDTO 查询请求体
     * @return 学生白名单列表
     */
    PageResult<SchoolStudentVO> getStudentWhitelist(SchoolStudentWhitelistQueryDTO queryDTO);


    void updateStudentWhitelist(SchoolStudentDTO studentWhitelistDTO);


    void deleteStudentWhitelist(Long id);


    void addStudentWhitelist(SchoolStudentDTO schoolStudentDTO);
}
