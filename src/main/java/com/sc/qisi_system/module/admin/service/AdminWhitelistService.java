package com.sc.qisi_system.module.admin.service;

import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.module.admin.dto.SchoolStaffDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStaffWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.vo.SchoolStaffVO;
import com.sc.qisi_system.module.admin.vo.SchoolStudentVO;

/**
 * 管理员白名单管理服务接口
 * 功能: 教职工白名单、学生白名单的查询、新增、修改、删除等业务逻辑处理
 */
public interface AdminWhitelistService {


    /**
     * 条件查询教职工白名单
     * 角色: 管理员
     *
     * @param queryDTO 教职工白名单查询条件
     * @return 教职工白名单分页列表
     */
    PageResult<SchoolStaffVO> getTeacherWhitelist(SchoolStaffWhitelistQueryDTO queryDTO);


    /**
     * 修改教职工白名单
     * 角色: 管理员
     *
     * @param schoolStaffDTO 教职工信息
     */
    void updateTeacherWhitelist(SchoolStaffDTO schoolStaffDTO);


    /**
     * 删除教职工白名单
     * 角色: 管理员
     *
     * @param id 教职工白名单ID
     */
    void deleteTeacherWhitelist(Long id);


    /**
     * 新增教职工白名单
     * 角色: 管理员
     *
     * @param schoolStaffDTO 教职工信息
     */
    void addTeacherWhitelist(SchoolStaffDTO schoolStaffDTO);


    /**
     * 条件查询学生白名单
     * 角色: 管理员
     *
     * @param queryDTO 学生白名单查询条件
     * @return 学生白名单分页列表
     */
    PageResult<SchoolStudentVO> getStudentWhitelist(SchoolStudentWhitelistQueryDTO queryDTO);


    /**
     * 修改学生白名单
     * 角色: 管理员
     *
     * @param studentWhitelistDTO 学生信息
     */
    void updateStudentWhitelist(SchoolStudentDTO studentWhitelistDTO);


    /**
     * 删除学生白名单
     * 角色: 管理员
     *
     * @param id 学生白名单ID
     */
    void deleteStudentWhitelist(Long id);


    /**
     * 新增学生白名单
     * 角色: 管理员
     *
     * @param schoolStudentDTO 学生信息
     */
    void addStudentWhitelist(SchoolStudentDTO schoolStudentDTO);
}