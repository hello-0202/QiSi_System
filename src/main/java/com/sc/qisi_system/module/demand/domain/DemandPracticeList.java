package com.sc.qisi_system.module.demand.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DemandPracticeList {

    /**
     * 需求id: 关联demand表
     */
    private Long demandId;


    /**
     * 状态：1-已通过 2-已退出 3-已移除 4-已归档
     */
    private Integer status;


    /**
     * 成员角色: 1-普通成员 2-负责人
     */
    private Integer roleType;


    /**
     * 加入时间
     */
    private LocalDateTime joinTime;


    /**
     * 退出/移除时间
     */
    private LocalDateTime quitTime;
}
