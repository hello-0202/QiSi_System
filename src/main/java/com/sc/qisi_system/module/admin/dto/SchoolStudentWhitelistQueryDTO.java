package com.sc.qisi_system.module.admin.dto;


import lombok.Data;

@Data
public class SchoolStudentWhitelistQueryDTO {


    /**
     * 起始页码
     */
    private Integer page = 1;


    /**
     * 每页展示数量
     */
    private Integer pageSize = 10;


    /**
     * 姓名
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
}
