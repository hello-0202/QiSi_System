package com.sc.qisi_system.module.admin.vo;


import lombok.Data;

@Data
public class SysUserTypeIdentityVO {

    /**
     * 用户类型：1-学生 2-教师 3-企业人员 4-管理员
     */
    private Integer userType;


    /**
     * 业务身份ID：1-认领者 2-发布者 3-管理员
     */
    private Integer identityId;
}
