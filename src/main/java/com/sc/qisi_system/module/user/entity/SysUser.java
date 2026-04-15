package com.sc.qisi_system.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("sys_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SysUser implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;


    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[A-Za-z\\d]{8,12}$", message = "密码格式不正确，请包含字母+数字，长度8-12位")
    private String password;


    /**
     * 姓名
     */
    private String name;


    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;


    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$",
            message = "邮箱格式不正确")
    private String email;


    /**
     * 头像
     */
    private String avatar;


    /**
     * 用户类型: 1-学生  2-教师  3-企业人员  5-管理员
     */
    private Integer userType;


    /**
     * 账号状态: 0-禁用 1-正常
     */
    private Boolean status;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
