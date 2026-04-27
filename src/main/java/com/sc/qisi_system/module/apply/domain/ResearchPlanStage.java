package com.sc.qisi_system.module.apply.domain;

import lombok.Data;

@Data
public class ResearchPlanStage {


    /**
     * 阶段标题
     */
    private String stage;


    /**
     * 阶段内容
     */
    private String content;


    /**
     * 是否完成
     */
    private Boolean completed;
}