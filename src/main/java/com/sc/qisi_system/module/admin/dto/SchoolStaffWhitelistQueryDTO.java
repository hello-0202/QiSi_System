package com.sc.qisi_system.module.admin.dto;


import lombok.Data;

@Data
public class SchoolStaffWhitelistQueryDTO {


    /**
     * 起始页码
     */
    private Integer page = 1;


    /**
     * 每页展示数量
     */
    private Integer pageSize = 10;


    /**
     * 人员代码
     */
    private String personCode;


    /**
     * 姓名
     */
    private String name;


    /**
     * 性别: 0=男 1=女
     */
    private Integer gender;


    /**
     * 现任单位
     */
    private String unitName;


    /**
     * 专业职称资格
     */
    private String professionalTitle;


    /**
     * 职级
     */
    private String rank;


    /**
     * 最高学位
     */
    private String highestDegree;


    /**
     * 来校时间
     */
    private String comeTime;
}
