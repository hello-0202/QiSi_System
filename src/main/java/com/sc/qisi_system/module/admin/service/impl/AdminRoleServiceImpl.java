package com.sc.qisi_system.module.admin.service.impl;

import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.mapper.SysUserTypeIdentityMapper;
import com.sc.qisi_system.module.admin.service.AdminRoleService;
import com.sc.qisi_system.module.admin.vo.SysUserTypeIdentityVO;
import com.sc.qisi_system.module.websocket.enumType.SystemPushTypeEnum;
import com.sc.qisi_system.module.websocket.service.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 管理员角色权限服务实现类
 */
@RequiredArgsConstructor
@Service
public class AdminRoleServiceImpl implements AdminRoleService {


    private final SysUserTypeIdentityMapper sysUserTypeIdentityMapper;
    private final WebSocketMessageService webSocketMessageService;


    /**
     * 查询用户类型与业务身份映射关系
     */
    @Override
    public List<SysUserTypeIdentityVO> getIdentityMap() {
        // 1. 查询所有映射配置
        List<SysUserTypeIdentity> sysUserTypeIdentityList = sysUserTypeIdentityMapper.selectList(null);

        // 2. 转换为VO并返回
        return sysUserTypeIdentityList.stream().map(list -> {
            SysUserTypeIdentityVO vo = new SysUserTypeIdentityVO();
            BeanUtils.copyProperties(list, vo);
            return vo;
        }).toList();
    }


    /**
     * 更新用户类型与业务身份映射关系
     */
    @Override
    public void updateIdentityMap(List<SysUserTypeIdentityVO> voList) {
        // 1. 清空原有所有映射关系
        sysUserTypeIdentityMapper.delete(null);

        // 2. 批量插入新的映射数据
        for (SysUserTypeIdentityVO vo : voList) {
            SysUserTypeIdentity entity = new SysUserTypeIdentity();
            BeanUtils.copyProperties(vo, entity);
            sysUserTypeIdentityMapper.insert(entity);
        }

        // 3. 广播更新通知
        webSocketMessageService.broadcastToAll(SystemPushTypeEnum.IDENTITY_MAP_UPDATED.getMsg());
    }
}