package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.service.AdminRoleService;
import com.sc.qisi_system.module.admin.vo.SysUserTypeIdentityVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色权限管理控制器
 * 功能: 用户类型与业务身份映射关系查询、修改/保存等角色权限配置操作
 */
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/role")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminRoleController {


    private final AdminRoleService adminRoleService;


    /**
     * 查询 (用户类型 → 业务身份) 映射关系接口
     * 角色: 管理员
     *
     * @return 用户类型与业务身份映射关系列表
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-identity-map")
    public Result getIdentityMap() {
        return Result.success(adminRoleService.getIdentityMap());
    }


    /**
     * 修改/保存 (用户类型 → 业务身份) 映射关系接口
     * 角色: 管理员
     *
     * @param voList 用户类型与业务身份映射关系列表
     * @return 统一返回结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-identity-map")
    public Result updateIdentityMap(
            @Valid @RequestBody List<SysUserTypeIdentityVO> voList) {
        adminRoleService.updateIdentityMap(voList);
        return Result.success();
    }
}