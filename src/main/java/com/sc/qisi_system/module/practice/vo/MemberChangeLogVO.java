package com.sc.qisi_system.module.practice.vo;

import com.sc.qisi_system.module.user.domain.UserInfoBase;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberChangeLogVO {


    /**
     * 变更用户ID
     */
    private Long userId;


    /**
     * 变更人信息
     */
    private UserInfoBase userInfoBase;


    /**
     * 操作人姓名
     */
    private String name;


    /**
     * 变更类型: 1-主动退出申请 2-发布者移除
     */
    private Integer changeType;


    /**
     * 审核状态: 0-待审核 1-已生效 2-已拒绝
     */
    private Integer status;


    /**
     * 变更原因
     */
    private String reason;


    /**
     * 审核人姓名
     */
    private String auditUserName;


    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
}
