package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师信息实体类
 * 对应数据库表：edu_teacher
 */
@TableName("edu_teacher")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EduTeacher implements Serializable {

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
     * 工号
     */
    private String teacherNo;

    /**
     * 性别
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
     * 研究方向
     */
    private String researchDirection;

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
