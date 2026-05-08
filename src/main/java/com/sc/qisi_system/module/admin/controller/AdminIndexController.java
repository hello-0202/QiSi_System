package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.dto.MenuDTO;
import com.sc.qisi_system.module.admin.dto.MenuQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminIndexService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/admin/index")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminIndexController {


    private final AdminIndexService adminIndexService;


    /**
     * 获取当前登录用户的业务身份
     */
    @GetMapping("/user-identity-list")
    public Result getUserIdentityList() {
        return Result.success(adminIndexService.getUserIdentity());
    }


    /**
     * 获取动态路由(按角色)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/routers")
    public Result getRouters(
            @RequestBody MenuQueryDTO menuQueryDTO) {
        return Result.success(adminIndexService.getRouters(menuQueryDTO));
    }


    /**
     * 条件查询所有菜单列表
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/menu/list")
    public Result getMenuList(
             @RequestBody MenuQueryDTO menuQueryDTO) {
        return Result.success(adminIndexService.getMenuRouteList(menuQueryDTO));
    }


    /**
     * 新增菜单
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/menu/add")
    public Result addMenu(
            @Valid @RequestBody MenuDTO menuDTO) {
        adminIndexService.addMenu(menuDTO);
        return Result.success();
    }


    /**
     * 修改菜单
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/menu/update")
    public Result updateMenu(
            @Valid @RequestBody MenuDTO menuDTO) {
        adminIndexService.updateMenu(menuDTO);
        return Result.success();
    }


    /**
     * 删除菜单
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/menu/delete")
    public Result deleteMenu(
            @RequestParam Long id) {
        adminIndexService.deleteMenu(id);
        return Result.success();
    }
}