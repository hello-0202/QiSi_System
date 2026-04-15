package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业实体类
 * 对应数据库表：enterprise
 */
@TableName("enterprise")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enterprise implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 企业名称
     */
    private String enterpriseName;


    /**
     * 统一社会信用代码
     */
    private String creditCode;


    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 合作类型: 1普通合作企业 2校企合作
     */
    private Integer cooperationType;

    /**
     * 状态：0-禁用 1-正常
     */
    private Boolean status;

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