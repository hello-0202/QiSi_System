package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 教职工白名单
 */
@TableName("school_staff")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SchoolStaff {


    @TableId(type = IdType.AUTO)
    private Long id;


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
     * 出生年月
     */
    private String birthYear;


    /**
     * 现任单位
     */
    private String unitName;


    /**
     * 民族
     */
    private String nation;


    /**
     * 专业职称资格
     */
    private String professionalTitle;


    /**
     * 职级
     */
    @TableField("`rank`")
    private String rank;


    /**
     * 最高学历
     */
    private String highestEducation;


    /**
     * 最高学位
     */
    private String highestDegree;


    /**
     * 来校时间
     */
    private String comeTime;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}