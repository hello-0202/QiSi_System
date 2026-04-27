package com.sc.qisi_system.module.practice.dto;


import lombok.Data;

@Data
public class QueryDemandProgressLogDTO {

    /**
     * 查询页数
     */
    private Integer pageNum = 1;


    /**
     * 查询数量
     */
    private Integer pageSize = 10;


    /**
     * 需求id
     */
    private Long demandId;
}
