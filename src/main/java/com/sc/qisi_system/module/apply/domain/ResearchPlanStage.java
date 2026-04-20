package com.sc.qisi_system.module.apply.domain;

import lombok.Data;

@Data
public class ResearchPlanStage {


    private String stage;     // 阶段一、阶段二


    private String content;   // 阶段内容


    private Boolean completed;// 是否完成（更新进度用）
}