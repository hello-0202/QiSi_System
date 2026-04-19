package com.sc.qisi_system.module.apply.dto;

import lombok.Data;

@Data
public class MyApplyQueryDTO {

    /**
     * 查询页数
     */
    private Integer pageNum = 1;


    /**
     * 查询数量
     */
    private Integer pageSize = 10;


    /**
     * 审核状态: 0-待审核 1-已通过 2-已拒绝
     */
    private Integer auditStatus;


}
