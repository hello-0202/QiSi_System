package com.sc.qisi_system.module.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 需求进度日志附件表实体类
 */

@TableName("demand_progress_attachment")
@Data
public class DemandProgressAttachment implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键id: 雪花算法
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 关联 demand_progress 表 id
     */
    private Long progressId;


    /**
     * 冗余字段：需求id
     */
    private Long demandId;


    /**
     * 附件上传人id: 关联sys_user表
     */
    private Long userId;


    /**
     * 文件名
     */
    private String fileName;


    /**
     * 存储路径
     */
    private String objectName;


    /**
     * 存储桶名称
     */
    private String bucketName;


    /**
     * 文件大小
     */
    private Long fileSize;


    /**
     * 文件类型
     */
    private String fileType;


    /**
     * 软删除标记字段: 0-未删除 1-已软删除
     */
    private Integer isDeleted;


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