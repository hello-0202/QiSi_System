package com.sc.qisi_system.module.apply.vo;

import com.sc.qisi_system.module.user.domain.UserInfoBase;
import lombok.Data;

@Data
public class ApplyMemberListVO {


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
}
