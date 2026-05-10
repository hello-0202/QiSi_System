package com.sc.qisi_system.module.admin.vo;

import com.sc.qisi_system.module.user.vo.UserProfileVO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class SysUserVO {


    /**
     * 用户扩展信息
     */
    private UserProfileVO userProfileVO;


    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;


    /**
     * 账号状态: 0-禁用 1-正常
     */
    private Boolean status;
}
