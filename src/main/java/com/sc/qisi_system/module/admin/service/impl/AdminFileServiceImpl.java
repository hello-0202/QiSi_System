package com.sc.qisi_system.module.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.sc.qisi_system.module.admin.dto.StudentWhitelistImportDTO;
import com.sc.qisi_system.module.admin.dto.TeacherWhitelistImportDTO;
import com.sc.qisi_system.module.admin.service.AdminFileService;
import com.sc.qisi_system.module.user.entity.SchoolStaff;
import com.sc.qisi_system.module.user.entity.SchoolStudent;
import com.sc.qisi_system.module.user.service.SchoolStaffService;
import com.sc.qisi_system.module.user.service.SchoolStudentService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AdminFileServiceImpl implements AdminFileService {


    private final SchoolStaffService schoolStaffService;
    private final SchoolStudentService schoolStudentService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importTeacherWhitelist(MultipartFile file) throws IOException {

        List<TeacherWhitelistImportDTO> excelList = EasyExcel
                .read(file.getInputStream())
                .head(TeacherWhitelistImportDTO.class)
                .sheet()
                .autoTrim(true)
                .doReadSync();

        List<SchoolStaff> list = new ArrayList<>();
        for (TeacherWhitelistImportDTO dto : excelList) {
            SchoolStaff staff = convertToSchoolStaff(dto);
            list.add(staff);
        }

        // 获取要导入的人员代码
        List<String> personCodes = list.stream()
                .map(SchoolStaff::getPersonCode)
                .collect(Collectors.toList());

        // 查询数据库已存在的
        List<String> existingCodes = schoolStaffService.lambdaQuery()
                .in(SchoolStaff::getPersonCode, personCodes)
                .list()
                .stream()
                .map(SchoolStaff::getPersonCode)
                .toList();

        // 过滤掉已存在的
        List<SchoolStaff> newList = list.stream()
                .filter(staff -> !existingCodes.contains(staff.getPersonCode()))
                .collect(Collectors.toList());

        if (!newList.isEmpty()) {
            schoolStaffService.saveBatch(newList);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importStudentWhitelist(MultipartFile file) throws IOException {
        List<StudentWhitelistImportDTO> excelList = EasyExcel
                .read(file.getInputStream())
                .head(StudentWhitelistImportDTO.class)
                .sheet()
                .autoTrim(true)
                .doReadSync();

        List<SchoolStudent> list = new ArrayList<>();
        for (StudentWhitelistImportDTO dto : excelList) {
            SchoolStudent student = convertToSchoolStudent(dto);
            list.add(student);
        }

        // 获取要导入的学号
        List<String> studentIds = list.stream()
                .map(SchoolStudent::getStudentId)
                .collect(Collectors.toList());

        List<String> existingIds = schoolStudentService.lambdaQuery()
                .in(SchoolStudent::getStudentId, studentIds)
                .list()
                .stream()
                .map(SchoolStudent::getStudentId)
                .toList();

        List<SchoolStudent> newList = list.stream()
                .filter(student -> !existingIds.contains(student.getStudentId()))
                .collect(Collectors.toList());

        if (!newList.isEmpty()) {
            schoolStudentService.saveBatch(newList);
        }
    }


    @NotNull
    private SchoolStaff convertToSchoolStaff(TeacherWhitelistImportDTO dto) {
        SchoolStaff staff = new SchoolStaff();


        staff.setPersonCode(dto.getPersonCode());
        if (dto.getPersonCode() == null || dto.getPersonCode().trim().isEmpty()) {
            String tempCode = "TEMP_" + System.nanoTime();
            staff.setPersonCode(tempCode);
        }
        staff.setName(dto.getName());

        if ("男".equals(dto.getGenderStr())) {
            staff.setGender(0);
        } else if ("女".equals(dto.getGenderStr())) {
            staff.setGender(1);
        }

        staff.setBirthYear(dto.getBirthYear());
        staff.setUnitName(dto.getUnitName());
        staff.setNation(dto.getNation());
        staff.setProfessionalTitle(dto.getProfessionalTitle());
        staff.setRank(dto.getRank());
        staff.setHighestEducation(dto.getHighestEducation());
        staff.setHighestDegree(dto.getHighestDegree());
        staff.setComeTime(dto.getComeTimeStr());
        return staff;
    }


    @NotNull
    private SchoolStudent convertToSchoolStudent(StudentWhitelistImportDTO dto) {
        SchoolStudent student = new SchoolStudent();

        if (dto.getStudentId() == null || dto.getStudentId().trim().isEmpty()) {
            student.setStudentId("TEMP_STU_" + System.nanoTime());
        } else {
            student.setStudentId(dto.getStudentId());
        }

        student.setName(dto.getName());
        student.setCollege(dto.getCollege());
        student.setMajor(dto.getMajor());
        student.setGrade(dto.getGrade());
        student.setEducationLevel(dto.getEducationLevel());
        student.setClassName(dto.getClassName());
        return student;
    }
}
