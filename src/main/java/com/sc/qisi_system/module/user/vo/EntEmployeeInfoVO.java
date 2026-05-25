package com.sc.qisi_system.module.user.vo;

import lombok.Data;


@Data
public class EntEmployeeInfoVO {


    /**
     * 关联用户ID: sys_user表主键
     */
    private Long userId;


    /**
     * 企业名称
     */
    private String enterpriseName;


    /**
     * 员工编号
     */
    private String employeeNo;


    /**
     * 手机号
     */
    private String phone;


    /**
     * 职位名称
     */
    private String jobTitle;


    /**
     * 部门
     */
    private String department;


    /**
     * 是否为企业联系人: 0-否 1-是
     */
    private Integer isContact;
}
