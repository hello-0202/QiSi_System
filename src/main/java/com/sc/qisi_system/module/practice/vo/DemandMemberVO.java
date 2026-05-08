package com.sc.qisi_system.module.practice.vo;

import com.sc.qisi_system.module.user.vo.UserProfileVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DemandMemberVO {


    /**
     * 成员id: 关联sys_user表
     */
    private Long userId;


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


    /**
     * 用户信息
     */
    private UserProfileVO userInfo;
}
