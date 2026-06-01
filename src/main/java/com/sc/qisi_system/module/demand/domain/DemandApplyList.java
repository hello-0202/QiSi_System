package com.sc.qisi_system.module.demand.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class DemandApplyList {


    /**
     * 主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    /**
     * 审核状态: 0-待审核 1-已通过 2-已拒绝
     */
    private Integer auditStatus;
}
