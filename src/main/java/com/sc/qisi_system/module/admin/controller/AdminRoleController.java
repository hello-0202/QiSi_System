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


@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/role")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminRoleController {


    private final AdminRoleService adminRoleService;


    /**
     * 查询 (用户类型 → 业务身份) 映射关系
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-identity-map")
    public Result getIdentityMap() {
        return Result.success(adminRoleService.getIdentityMap());
    }


    /**
     * 修改/保存 (用户类型 → 业务身份) 映射关系
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-identity-map")
    public Result updateIdentityMap(
            @Valid @RequestBody List<SysUserTypeIdentityVO> voList) {
        adminRoleService.updateIdentityMap(voList);
        return Result.success();
    }
}