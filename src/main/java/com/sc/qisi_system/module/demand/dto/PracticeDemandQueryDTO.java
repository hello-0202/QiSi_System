package com.sc.qisi_system.module.demand.dto;

import lombok.Data;

import java.util.List;

@Data
public class PracticeDemandQueryDTO {


    /**
     * 查询页数
     */
    private Integer pageNum = 1;


    /**
     * 查询数量
     */
    private Integer pageSize = 10;


    /**
     * 需求状态: 0-草稿 1审核中 2-已驳回 3-已发布 4-研究中 5-已完成 6-已关闭
     */
    private List<Integer> statusList;
}
