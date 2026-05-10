package com.sc.qisi_system.module.admin.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SchoolStudentDTO {


    /**
     * 主键id
     */
    @NotNull
    private Long id;


    /**
     * 学生姓名
     */
    @NotBlank
    private String name;


    /**
     * 学号
     */
    @NotBlank
    private String studentId;


    /**
     * 学院
     */
    @NotBlank
    private String college;


    /**
     * 专业
     */
    @NotBlank
    private String major;


    /**
     * 年级
     */
    @NotBlank
    private String grade;


    /**
     * 学历层次
     */
    @NotBlank
    private String educationLevel;


    /**
     * 班级
     */
    @NotBlank
    private String className;
}
