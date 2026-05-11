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


/**
 * 管理员文件导入服务实现类
 */
@RequiredArgsConstructor
@Service
public class AdminFileServiceImpl implements AdminFileService {


    private final SchoolStaffService schoolStaffService;
    private final SchoolStudentService schoolStudentService;


    /**
     * 导入教职工白名单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importTeacherWhitelist(MultipartFile file) throws IOException {
        // 1. 读取Excel文件数据
        List<TeacherWhitelistImportDTO> excelList = EasyExcel
                .read(file.getInputStream())
                .head(TeacherWhitelistImportDTO.class)
                .sheet()
                .autoTrim(true)
                .doReadSync();

        // 2. 转换DTO为实体对象
        List<SchoolStaff> list = new ArrayList<>();
        for (TeacherWhitelistImportDTO dto : excelList) {
            SchoolStaff staff = convertToSchoolStaff(dto);
            list.add(staff);
        }

        // 3. 提取待导入人员编码
        List<String> personCodes = list.stream()
                .map(SchoolStaff::getPersonCode)
                .collect(Collectors.toList());

        // 4. 查询数据库中已存在的人员编码
        List<String> existingCodes = schoolStaffService.lambdaQuery()
                .in(SchoolStaff::getPersonCode, personCodes)
                .list()
                .stream()
                .map(SchoolStaff::getPersonCode)
                .toList();

        // 5. 过滤掉已存在的数据，只保留新增数据
        List<SchoolStaff> newList = list.stream()
                .filter(staff -> !existingCodes.contains(staff.getPersonCode()))
                .collect(Collectors.toList());

        // 6. 批量保存新数据
        if (!newList.isEmpty()) {
            schoolStaffService.saveBatch(newList);
        }
    }


    /**
     * 导入学生白名单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importStudentWhitelist(MultipartFile file) throws IOException {
        // 1. 读取Excel文件数据
        List<StudentWhitelistImportDTO> excelList = EasyExcel
                .read(file.getInputStream())
                .head(StudentWhitelistImportDTO.class)
                .sheet()
                .autoTrim(true)
                .doReadSync();

        // 2. 转换DTO为实体对象
        List<SchoolStudent> list = new ArrayList<>();
        for (StudentWhitelistImportDTO dto : excelList) {
            SchoolStudent student = convertToSchoolStudent(dto);
            list.add(student);
        }

        // 3. 提取待导入学号
        List<String> studentIds = list.stream()
                .map(SchoolStudent::getStudentId)
                .collect(Collectors.toList());

        // 4. 查询数据库中已存在的学号
        List<String> existingIds = schoolStudentService.lambdaQuery()
                .in(SchoolStudent::getStudentId, studentIds)
                .list()
                .stream()
                .map(SchoolStudent::getStudentId)
                .toList();

        // 5. 过滤掉已存在的数据，只保留新增数据
        List<SchoolStudent> newList = list.stream()
                .filter(student -> !existingIds.contains(student.getStudentId()))
                .collect(Collectors.toList());

        // 6. 批量保存新数据
        if (!newList.isEmpty()) {
            schoolStudentService.saveBatch(newList);
        }
    }


    /**
     * 转换导入DTO为教职工实体
     */
    @NotNull
    private SchoolStaff convertToSchoolStaff(TeacherWhitelistImportDTO dto) {
        // 1. 创建教职工对象并设置基础信息
        SchoolStaff staff = new SchoolStaff();
        staff.setPersonCode(dto.getPersonCode());

        // 2. 处理空人员编码，生成临时编码
        if (dto.getPersonCode() == null || dto.getPersonCode().trim().isEmpty()) {
            String tempCode = "TEMP_" + System.nanoTime();
            staff.setPersonCode(tempCode);
        }

        // 3. 设置姓名
        staff.setName(dto.getName());

        // 4. 转换性别文字为数字编码
        if ("男".equals(dto.getGenderStr())) {
            staff.setGender(0);
        } else if ("女".equals(dto.getGenderStr())) {
            staff.setGender(1);
        }

        // 5. 设置其他扩展信息
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


    /**
     * 转换导入DTO为学生实体
     */
    @NotNull
    private SchoolStudent convertToSchoolStudent(StudentWhitelistImportDTO dto) {
        // 1. 创建学生对象
        SchoolStudent student = new SchoolStudent();

        // 2. 处理空学号，生成临时编码
        if (dto.getStudentId() == null || dto.getStudentId().trim().isEmpty()) {
            student.setStudentId("TEMP_STU_" + System.nanoTime());
        } else {
            student.setStudentId(dto.getStudentId());
        }

        // 3. 设置学生基础信息
        student.setName(dto.getName());
        student.setCollege(dto.getCollege());
        student.setMajor(dto.getMajor());
        student.setGrade(dto.getGrade());
        student.setEducationLevel(dto.getEducationLevel());
        student.setClassName(dto.getClassName());

        return student;
    }
}