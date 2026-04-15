package com.sc.qisi_system.module.demand.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicableDemandQueryDTO {


    /**
     * 查询页数
     */
    private Integer pageNum = 1;


    /**
     * 查询数量
     */
    private Integer pageSize = 10;


    /**
     * 需求分类
     */
    private Integer category;


    /**
     * 是否需要提交方案: 0-不需要 1-需要
     */
    private Boolean requirePlan;


    /**
     * 申请截止时间
     */
    private LocalDateTime deadline;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
