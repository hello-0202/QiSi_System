package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.module.admin.dto.MenuDTO;
import com.sc.qisi_system.module.admin.dto.MenuQueryDTO;
import com.sc.qisi_system.module.admin.service.AdminIndexService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单管理控制器
 * 功能: 获取用户身份、动态路由、菜单列表查询、菜单新增/修改/删除等操作
 */
@RequestMapping("/api/admin/index")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminIndexController {


    private final AdminIndexService adminIndexService;


    /**
     * 条件查询所有菜单列表接口
     * 角色: 管理员
     *
     * @param menuQueryDTO 菜单查询条件
     * @return 菜单列表数据
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/menu/list")
    public Result getMenuList(
            @RequestBody MenuQueryDTO menuQueryDTO) {
        return Result.success(adminIndexService.getMenuRouteList(menuQueryDTO));
    }


    /**
     * 添加菜单与业务身份绑定接口
     * 角色: 管理员
     *
     * @param menuId 菜单ID
     * @param identityId 业务身份ID
     * @return 统一返回结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/menu/bind-identity")
    public Result bindMenuIdentity(
             @RequestParam @NotNull(message = "菜单ID不能为空") Long menuId,
             @RequestParam @NotNull(message = "身份ID不能为空") Integer identityId) {
        adminIndexService.bindMenuIdentity(menuId, identityId);
        return Result.success();
    }


    /**
     * 将菜单与业务身份解绑接口
     * 角色: 管理员
     *
     * @param id 主键id
     * @return 统一返回结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/menu/unbind-identity")
    public Result unbindMenuIdentity(
             @RequestParam @NotNull(message = "ID不能为空") Long id) {
        adminIndexService.unbindMenuIdentity(id);
        return Result.success();
    }


    /**
     * 新增菜单接口
     * 角色: 管理员
     *
     * @param menuDTO 菜单信息
     * @return 统一返回结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/menu/add")
    public Result addMenu(
            @Valid @RequestBody MenuDTO menuDTO) {
        adminIndexService.addMenu(menuDTO);
        return Result.success();
    }


    /**
     * 修改菜单接口
     * 角色: 管理员
     *
     * @param menuDTO 菜单信息
     * @return 统一返回结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/menu/update")
    public Result updateMenu(
            @Valid @RequestBody MenuDTO menuDTO) {
        adminIndexService.updateMenu(menuDTO);
        return Result.success();
    }


    /**
     * 删除菜单接口
     * 角色: 管理员
     *
     * @param id 菜单ID
     * @return 统一返回结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/menu/delete")
    public Result deleteMenu(
            @RequestParam Long id) {
        adminIndexService.deleteMenu(id);
        return Result.success();
    }
}