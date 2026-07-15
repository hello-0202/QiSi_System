package com.sc.qisi_system.module.practice.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sc.qisi_system.module.user.vo.UserProfileVO;
import lombok.Data;

@Data
public class MemberVO {


    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    /**
     * 申请id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applyId;


    /**
     * 审核状态: 0-待审核 1-已通过 2-已拒绝
     */
    private Integer auditStatus;


    /**
     * 头像
     */
    private String avatarUrl;


    /**
     * 用户信息
     */
    private UserProfileVO userProfileVO;


    /**
     * 用户成员信息
     */
    private DemandMemberVO demandMemberVO;
}
