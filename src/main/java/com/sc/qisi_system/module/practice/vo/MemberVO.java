package com.sc.qisi_system.module.practice.vo;

import com.sc.qisi_system.module.user.domain.UserInfoBase;
import lombok.Data;

@Data
public class MemberVO {


    /**
     * 用户id
     */
    private Long id;


    /**
     * 申请id
     */
    private Long applyId;


    /**
     * 用户信息
     */
    private UserInfoBase userInfoBase;


    /**
     * 用户成员信息
     */
    private DemandMemberVO demandMemberVO;
}
