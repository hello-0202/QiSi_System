package com.sc.qisi_system.module.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminDemandQueryDTO {


    /**
     * 查询页数
     */
    private Integer pageNum = 1;


    /**
     * 查询数量
     */
    private Integer pageSize = 10;


    /**
     * 姓名
     */
    private String name;


    /**
     * 需求分类
     */
    private Integer category;


    /**
     * 需求状态: 0-草稿 1审核中 2-已驳回 3-已发布 4-研究中 5-已完成 6-已关闭
     */
    private Integer status;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
