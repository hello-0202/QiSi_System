package com.sc.qisi_system.module.practice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MemberChangeDTO {


    /**
     * 关联需求ID
     */
    @NotNull
    private Long demandId;


    /**
     * 变更用户ID
     */
    @NotNull
    private Long userId;


    /**
     * 变更原因
     */
    @NotBlank
    private String reason;


    /**
     * 审核状态: 0-待审核 1-已生效 2-已拒绝
     */
    private Integer status;
}
