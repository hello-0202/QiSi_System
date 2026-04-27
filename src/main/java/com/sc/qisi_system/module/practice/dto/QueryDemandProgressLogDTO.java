package com.sc.qisi_system.module.practice.dto;


import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long demandId;
}
