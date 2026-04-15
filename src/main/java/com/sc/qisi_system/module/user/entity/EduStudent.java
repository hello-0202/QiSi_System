package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生信息实体类
 * 对应数据库表：edu_student
 */
@TableName("edu_student")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EduStudent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 关联用户ID: sys_user表主键
     */
    private Long userId;


    /**
     * 学号
     */
    private String studentNo;


    /**
     * 学院
     */
    private String college;


    /**
     * 专业
     */
    private String major;


    /**
     * 班级
     */
    private String className;


    /**
     * 年级
     */
    private String grade;


    /**
     * 性别
     */
    private Integer gender;


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
