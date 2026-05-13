package com.sc.qisi_system.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.PageResult;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.module.admin.dto.SchoolStaffDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStaffWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentDTO;
import com.sc.qisi_system.module.admin.dto.SchoolStudentWhitelistQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminWhitelistService;
import com.sc.qisi_system.module.admin.vo.SchoolStaffVO;
import com.sc.qisi_system.module.admin.vo.SchoolStudentVO;
import com.sc.qisi_system.module.user.entity.SchoolStaff;
import com.sc.qisi_system.module.user.entity.SchoolStudent;
import com.sc.qisi_system.module.user.service.SchoolStaffService;
import com.sc.qisi_system.module.user.service.SchoolStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 管理员白名单管理服务实现类
 */
@RequiredArgsConstructor
@Service
public class AdminWhitelistServiceImpl implements AdminWhitelistService {


    private final SchoolStudentService schoolStudentService;
    private final SchoolStaffService schoolStaffService;


    /**
     * 条件查询教职工白名单
     */
    @Override
    public PageResult<SchoolStaffVO> getTeacherWhitelist(SchoolStaffWhitelistQueryDTO queryDTO) {
        // 1. 构建分页对象
        Page<SchoolStaff> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        // 2. 构建动态查询条件
        LambdaQueryWrapper<SchoolStaff> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                // 字符串类型：必须用 StringUtils.hasText
                .eq(StringUtils.hasText(queryDTO.getPersonCode()), SchoolStaff::getPersonCode, queryDTO.getPersonCode())
                .eq(StringUtils.hasText(queryDTO.getName()), SchoolStaff::getName, queryDTO.getName())
                .eq(StringUtils.hasText(queryDTO.getUnitName()), SchoolStaff::getUnitName, queryDTO.getUnitName())
                .eq(StringUtils.hasText(queryDTO.getProfessionalTitle()), SchoolStaff::getProfessionalTitle, queryDTO.getProfessionalTitle())
                .eq(StringUtils.hasText(queryDTO.getRank()), SchoolStaff::getRank, queryDTO.getRank())
                .eq(StringUtils.hasText(queryDTO.getHighestDegree()), SchoolStaff::getHighestDegree, queryDTO.getHighestDegree())

                .eq(queryDTO.getGender() != null, SchoolStaff::getGender, queryDTO.getGender())
                .eq(StringUtils.hasText(queryDTO.getComeTime()), SchoolStaff::getComeTime, queryDTO.getComeTime());

        // 3. 分页查询数据
        IPage<SchoolStaff> schoolStaffIpage = schoolStaffService.page(page, queryWrapper);

        // 4. 转换为VO列表
        List<SchoolStaffVO> schoolStaffVOS = schoolStaffIpage.getRecords().stream().map(schoolStaff -> {
            SchoolStaffVO schoolStaffVO = new SchoolStaffVO();
            BeanUtils.copyProperties(schoolStaff, schoolStaffVO);
            return schoolStaffVO;
        }).toList();

        // 5. 封装分页结果返回
        PageResult<SchoolStaffVO> pageResult = new PageResult<>();
        pageResult.setRecords(schoolStaffVOS);
        pageResult.setTotal(schoolStaffIpage.getTotal());
        pageResult.setPages(schoolStaffIpage.getPages());

        return pageResult;
    }

    /**
     * 修改教职工白名单
     */
    @Override
    public void updateTeacherWhitelist(SchoolStaffDTO schoolStaffDTO) {
        // 1. 校验数据是否存在
        SchoolStaff schoolStaff = schoolStaffService.getById(schoolStaffDTO.getId());
        if (schoolStaff == null) {
            throw new BusinessException(ResultCode.TEACHER_WHITELIST_NOT_EXIST);
        }

        // 2. 复制属性并更新
        BeanUtils.copyProperties(schoolStaffDTO, schoolStaff);
        schoolStaffService.updateById(schoolStaff);
    }


    /**
     * 删除教职工白名单
     */
    @Override
    public void deleteTeacherWhitelist(Long id) {
        // 1. 校验数据是否存在
        if (!schoolStaffService.exists(Wrappers
                .lambdaQuery(SchoolStaff.class)
                .eq(SchoolStaff::getId, id))) {
            throw new BusinessException(ResultCode.TEACHER_WHITELIST_NOT_EXIST);
        }

        // 2. 执行删除
        schoolStaffService.removeById(id);
    }


    /**
     * 新增教职工白名单
     */
    @Override
    public void addTeacherWhitelist(SchoolStaffDTO schoolStaffDTO) {
        // 1. 校验人员编码是否已存在
        if (schoolStaffService.exists(Wrappers
                .lambdaQuery(SchoolStaff.class)
                .eq(SchoolStaff::getPersonCode, schoolStaffDTO.getPersonCode()))) {
            throw new BusinessException(ResultCode.TEACHER_WHITELIST_EXISTED);
        }

        // 2. 转换并保存
        SchoolStaff schoolStaff = new SchoolStaff();
        BeanUtils.copyProperties(schoolStaffDTO, schoolStaff);
        schoolStaffService.save(schoolStaff);
    }


    /**
     * 条件查询学生白名单
     */
    @Override
    public PageResult<SchoolStudentVO> getStudentWhitelist(SchoolStudentWhitelistQueryDTO queryDTO) {
        // 1. 构建分页对象
        Page<SchoolStudent> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        // 2. 构建动态查询条件
        LambdaQueryWrapper<SchoolStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(StringUtils.hasText(queryDTO.getStudentId()), SchoolStudent::getStudentId, queryDTO.getStudentId())
                .eq(StringUtils.hasText(queryDTO.getName()), SchoolStudent::getName, queryDTO.getName())
                .eq(StringUtils.hasText(queryDTO.getCollege()), SchoolStudent::getCollege, queryDTO.getCollege())
                .eq(StringUtils.hasText(queryDTO.getMajor()), SchoolStudent::getMajor, queryDTO.getMajor())
                .eq(StringUtils.hasText(queryDTO.getGrade()), SchoolStudent::getGrade, queryDTO.getGrade());

        // 3. 分页查询数据
        IPage<SchoolStudent> schoolStudentIPage = schoolStudentService.page(page, queryWrapper);

        // 4. 转换为VO列表
        List<SchoolStudentVO> schoolStudentVOS = schoolStudentIPage.getRecords().stream().map(schoolStudent -> {
            SchoolStudentVO schoolStudentVO = new SchoolStudentVO();
            BeanUtils.copyProperties(schoolStudent, schoolStudentVO);
            return schoolStudentVO;
        }).toList();

        // 5. 封装分页结果返回
        PageResult<SchoolStudentVO> pageResult = new PageResult<>();
        pageResult.setRecords(schoolStudentVOS);
        pageResult.setTotal(schoolStudentIPage.getTotal());
        pageResult.setPages(schoolStudentIPage.getPages());

        return pageResult;
    }


    /**
     * 修改学生白名单
     */
    @Override
    public void updateStudentWhitelist(SchoolStudentDTO schoolStudentDTO) {
        // 1. 校验数据是否存在
        SchoolStudent schoolStudent = schoolStudentService.getById(schoolStudentDTO.getId());
        if (schoolStudent == null) {
            throw new BusinessException(ResultCode.STUDENT_WHITELIST_NOT_EXIST);
        }

        // 2. 复制属性并更新
        BeanUtils.copyProperties(schoolStudentDTO, schoolStudent);
        schoolStudentService.updateById(schoolStudent);
    }


    /**
     * 删除学生白名单
     */
    @Override
    public void deleteStudentWhitelist(Long id) {
        // 1. 校验数据是否存在
        if (!schoolStudentService.exists(Wrappers
                .lambdaQuery(SchoolStudent.class)
                .eq(SchoolStudent::getId, id))) {
            throw new BusinessException(ResultCode.STUDENT_WHITELIST_NOT_EXIST);
        }

        // 2. 执行删除
        schoolStudentService.removeById(id);
    }


    /**
     * 新增学生白名单
     */
    @Override
    public void addStudentWhitelist(SchoolStudentDTO schoolStudentDTO) {
        // 1. 校验学号是否已存在
        if (schoolStudentService.exists(Wrappers
                .lambdaQuery(SchoolStudent.class)
                .eq(SchoolStudent::getStudentId, schoolStudentDTO.getStudentId()))) {
            throw new BusinessException(ResultCode.STUDENT_WHITELIST_EXISTED);
        }

        // 2. 转换并保存
        SchoolStudent schoolStudent = new SchoolStudent();
        BeanUtils.copyProperties(schoolStudentDTO, schoolStudent);
        schoolStudentService.save(schoolStudent);
    }
}