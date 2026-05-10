package com.sc.qisi_system.module.admin.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class SysUserQueryDTO {


    /**
     * 起始页码
     */
    private Integer page = 1;


    /**
     * 每页展示数量
     */
    private Integer pageSize = 10;


    /**
     * 用户名
     */
    private String username;


    /**
     * 姓名
     */
    private String name;


    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;


    /**
     * 用户类型：1-学生 2-教师 3-企业人员 4-管理员
     */
    private Integer userType;


    /**
     * 账号状态: 0-禁用 1-正常
     */
    private Boolean status;
}
