package com.sc.qisi_system.module.admin.vo;

import lombok.Data;

@Data
public class SchoolStudentVO {


    /**
     * 学生id
     */
    private Long id;


    /**
     * 学生姓名
     */
    private String name;


    /**
     * 学号
     */
    private String studentId;


    /**
     * 学院
     */
    private String college;


    /**
     * 专业
     */
    private String major;


    /**
     * 年级
     */
    private String grade;


    /**
     * 学历层次
     */
    private String educationLevel;


    /**
     * 班级
     */
    private String className;
}
