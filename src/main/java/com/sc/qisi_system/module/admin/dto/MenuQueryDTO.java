package com.sc.qisi_system.module.admin.dto;

import lombok.Data;


@Data
public class MenuQueryDTO {


    /**
     * 查询页数
     */
    private Integer pageNum = 1;


    /**
     * 查询数量
     */
    private Integer pageSize = 10;


    /**
     * 父菜单ID 0为一级菜单
     */
    private Long parentId;


    /**
     * 业务身份ID / 角色ID
     * 对应 sys_identity.id
     */
    private Integer identityId;
}
