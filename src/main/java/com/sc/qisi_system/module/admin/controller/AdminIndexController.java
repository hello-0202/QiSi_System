package com.sc.qisi_system.module.admin.controller;

import com.sc.qisi_system.common.result.Result;
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


    //TODO
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/profile")
    public Result getProfile() {
        return Result.success(null);
    }


    //TODO
    /**
     * 获取动态路由（前端侧边栏菜单）
     */
    @GetMapping("/routers")
    public Result getRouters() {
        return Result.success(null);
    }


    //TODO
    /**
     * 查询所有菜单列表（用于菜单管理页面）
     */
    @GetMapping("/menu/list")
    public Result getMenuList() {
        return Result.success(null);
    }


    //TODO
    /**
     * 新增菜单
     */
    @PostMapping("/menu/add")
    public Result addMenu() {
        return Result.success("新增成功");
    }


    //TODO
    /**
     * 修改菜单
     */
    @PutMapping("/menu/update")
    public Result updateMenu() {
        return Result.success("修改成功");
    }


    //TODO
    /**
     * 删除菜单
     */
    @DeleteMapping("/menu/delete")
    public Result deleteMenu() {
        return Result.success("删除成功");
    }

}