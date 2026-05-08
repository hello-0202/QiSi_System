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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

        try {
            schoolStaffService.saveBatch(list);
        } catch (DuplicateKeyException ignored) {
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

        try {
            schoolStudentService.saveBatch(list);
        } catch (DuplicateKeyException ignored) {
        }
    }


    @NotNull
    private SchoolStaff convertToSchoolStaff(TeacherWhitelistImportDTO dto) {
        SchoolStaff staff = new SchoolStaff();
        staff.setPersonCode(dto.getPersonCode());
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
        student.setName(dto.getName());
        student.setStudentId(dto.getStudentId());
        student.setCollege(dto.getCollege());
        student.setMajor(dto.getMajor());
        student.setGrade(dto.getGrade());
        student.setEducationLevel(dto.getEducationLevel());
        student.setClassName(dto.getClassName());
        return student;
    }
}
