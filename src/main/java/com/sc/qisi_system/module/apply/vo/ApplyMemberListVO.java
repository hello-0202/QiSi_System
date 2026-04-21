package com.sc.qisi_system.module.apply.vo;

import com.sc.qisi_system.module.apply.domain.EnterpriseInfo;
import com.sc.qisi_system.module.apply.domain.StudentInfo;
import com.sc.qisi_system.module.apply.domain.TeacherInfo;
import lombok.Data;

@Data
public class ApplyMemberListVO {


    /**
     * 用户id
     */
    private Long id;


    /**
     * 申请id
     */
    private Long applyId;


    /**
     * 姓名
     */
    private String name;


    /**
     * 用户类型: 1-学生  2-教师  3-企业人员  5-管理员
     */
    private Integer userType;


    /**
     * 头像
     */
    private String avatar;


    /**
     * 学生信息
     */
    private StudentInfo studentInfo;


    /**
     * 教师信息
     */
    private TeacherInfo teacherInfo;


    /**
     * 企业人员信息
     */
    private EnterpriseInfo enterpriseInfo;


    /**
     * 手机号
     */
    private String phone;


    /**
     * 邮箱
     */
    private String email;
}
