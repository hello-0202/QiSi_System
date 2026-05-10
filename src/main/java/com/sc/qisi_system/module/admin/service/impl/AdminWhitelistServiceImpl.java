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

import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminWhitelistServiceImpl implements AdminWhitelistService {


    private final SchoolStudentService schoolStudentService;
    private final SchoolStaffService schoolStaffService;


    @Override
    public PageResult<SchoolStaffVO> getTeacherWhitelist(SchoolStaffWhitelistQueryDTO queryDTO) {

        Page<SchoolStaff> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        LambdaQueryWrapper<SchoolStaff> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(queryDTO.getPersonCode() != null, SchoolStaff::getPersonCode, queryDTO.getPersonCode())
                .eq(queryDTO.getName() != null, SchoolStaff::getName, queryDTO.getName())
                .eq(queryDTO.getGender() != null, SchoolStaff::getGender, queryDTO.getGender())
                .eq(queryDTO.getUnitName() != null, SchoolStaff::getUnitName, queryDTO.getUnitName())
                .eq(queryDTO.getProfessionalTitle() != null, SchoolStaff::getProfessionalTitle, queryDTO.getProfessionalTitle())
                .eq(queryDTO.getRank() != null, SchoolStaff::getRank, queryDTO.getRank())
                .eq(queryDTO.getHighestDegree() != null, SchoolStaff::getHighestDegree, queryDTO.getHighestDegree())
                .eq(queryDTO.getComeTime() != null, SchoolStaff::getComeTime, queryDTO.getComeTime());
        IPage<SchoolStaff> schoolStaffIpage = schoolStaffService.page(page,queryWrapper);
        List<SchoolStaffVO> schoolStaffVOS = schoolStaffIpage.getRecords().stream().map(
                schoolStaff -> {
                    SchoolStaffVO schoolStaffVO = new SchoolStaffVO();
                    BeanUtils.copyProperties(schoolStaff, schoolStaffVO);
                    return schoolStaffVO;
                }).toList();

        PageResult<SchoolStaffVO> pageResult = new PageResult<>();
        pageResult.setRecords(schoolStaffVOS);
        pageResult.setTotal(schoolStaffIpage.getTotal());
        pageResult.setPages(schoolStaffIpage.getPages());

        return pageResult;
    }


    @Override
    public void updateTeacherWhitelist(SchoolStaffDTO schoolStaffDTO) {

        SchoolStaff schoolStaff = schoolStaffService.getById(schoolStaffDTO.getId());
        if(schoolStaff == null) {
            throw new BusinessException(ResultCode.TEACHER_WHITELIST_NOT_EXIST);
        }
        BeanUtils.copyProperties(schoolStaffDTO, schoolStaff);
        schoolStaffService.updateById(schoolStaff);
    }


    @Override
    public void deleteTeacherWhitelist(Long id) {

        if(!schoolStaffService.exists(Wrappers
                .lambdaQuery(SchoolStaff.class)
                .eq(SchoolStaff::getId,id))){
            throw new BusinessException(ResultCode.TEACHER_WHITELIST_NOT_EXIST);
        }
        schoolStaffService.removeById(id);
    }


    @Override
    public void addTeacherWhitelist(SchoolStaffDTO schoolStaffDTO) {

        if(schoolStaffService.exists(Wrappers
                .lambdaQuery(SchoolStaff.class)
                .eq(SchoolStaff::getPersonCode,schoolStaffDTO.getPersonCode()))){
            throw new BusinessException(ResultCode.TEACHER_WHITELIST_EXISTED);
        }
        SchoolStaff schoolStaff = new SchoolStaff();
        BeanUtils.copyProperties(schoolStaffDTO,schoolStaff);
        schoolStaffService.save(schoolStaff);
    }


    @Override
    public PageResult<SchoolStudentVO> getStudentWhitelist(SchoolStudentWhitelistQueryDTO queryDTO) {

        Page<SchoolStudent> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        LambdaQueryWrapper<SchoolStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(queryDTO.getStudentId() != null, SchoolStudent::getStudentId, queryDTO.getStudentId())
                .eq(queryDTO.getName() != null, SchoolStudent::getName, queryDTO.getName())
                .eq(queryDTO.getCollege() != null,SchoolStudent::getCollege, queryDTO.getCollege())
                .eq(queryDTO.getMajor() != null,SchoolStudent::getMajor, queryDTO.getMajor())
                .eq(queryDTO.getGrade() != null,SchoolStudent::getGrade, queryDTO.getGrade());
        IPage<SchoolStudent> schoolStudentIPage = schoolStudentService.page(page,queryWrapper);
        List<SchoolStudentVO> schoolStudentVOS = schoolStudentIPage.getRecords().stream().map(
                schoolStudent -> {
                    SchoolStudentVO schoolStudentVO = new SchoolStudentVO();
                    BeanUtils.copyProperties(schoolStudent, schoolStudentVO);
                    return schoolStudentVO;
                }).toList();

        PageResult<SchoolStudentVO> pageResult = new PageResult<>();
        pageResult.setRecords(schoolStudentVOS);
        pageResult.setTotal(schoolStudentIPage.getTotal());
        pageResult.setPages(schoolStudentIPage.getPages());

        return pageResult;
    }


    @Override
    public void updateStudentWhitelist(SchoolStudentDTO schoolStudentDTO) {

        SchoolStudent schoolStudent = schoolStudentService.getById(schoolStudentDTO.getId());
        if(schoolStudent == null){
            throw new BusinessException(ResultCode.STUDENT_WHITELIST_NOT_EXIST);
        }
        BeanUtils.copyProperties(schoolStudentDTO,schoolStudent);
        schoolStudentService.updateById(schoolStudent);
    }


    @Override
    public void deleteStudentWhitelist(Long id) {

        if(!schoolStudentService.exists(Wrappers
                .lambdaQuery(SchoolStudent.class)
                .eq(SchoolStudent::getId,id))){
            throw new BusinessException(ResultCode.STUDENT_WHITELIST_NOT_EXIST);
        }
        schoolStudentService.removeById(id);
    }


    @Override
    public void addStudentWhitelist(SchoolStudentDTO schoolStudentDTO) {

        if(schoolStudentService.exists(Wrappers
                .lambdaQuery(SchoolStudent.class)
                .eq(SchoolStudent::getStudentId,schoolStudentDTO.getStudentId()))){
            throw new BusinessException(ResultCode.STUDENT_WHITELIST_EXISTED);
        }
        SchoolStudent schoolStudent = new SchoolStudent();
        BeanUtils.copyProperties(schoolStudentDTO,schoolStudent);
        schoolStudentService.save(schoolStudent);
    }
}
