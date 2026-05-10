package com.sc.qisi_system.module.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SchoolStaffDTO {


    /**
     * 主键id
     */
    @NotNull
    private Long id;


    /**
     * 人员代码
     */
    @NotBlank
    private String personCode;


    /**
     * 姓名
     */
    @NotBlank
    private String name;


    /**
     * 性别: 0=男 1=女
     */
    @NotNull
    private Integer gender;


    /**
     * 出生年月
     */
    @NotBlank
    private String birthYear;


    /**
     * 现任单位
     */
    @NotBlank
    private String unitName;


    /**
     * 民族
     */
    @NotBlank
    private String nation;


    /**
     * 专业职称资格
     */
    @NotBlank
    private String professionalTitle;


    /**
     * 职级
     */
    @NotBlank
    private String rank;


    /**
     * 最高学历
     */
    @NotBlank
    private String highestEducation;


    /**
     * 最高学位
     */
    @NotBlank
    private String highestDegree;


    /**
     * 来校时间
     */
    @NotBlank
    private String comeTime;
}
