package com.sc.qisi_system.module.demand.domain;

import lombok.Data;

@Data
public class DemandPublisherList {


    /**
     * 头像
     */
    private String avatarUrl;


    /**
     * 姓名
     */
    private String name;


    /**
     * 用户类型: 1-学生  2-教师  3-企业人员  5-管理员
     */
    private Integer userType;


    /**
     * 现任单位
     */
    private String unitName;


    /**
     * 学院
     */
    private String college;


    /**
     * 企业名称
     */
    private String enterpriseName;
}
