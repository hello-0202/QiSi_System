package com.sc.qisi_system.module.admin.service.impl;

import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.mapper.SysUserTypeIdentityMapper;
import com.sc.qisi_system.module.admin.service.AdminRoleService;
import com.sc.qisi_system.module.admin.vo.SysUserTypeIdentityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminRoleServiceImpl implements AdminRoleService {


    private final SysUserTypeIdentityMapper sysUserTypeIdentityMapper;


    @Override
    public List<SysUserTypeIdentityVO> getIdentityMap() {

        List<SysUserTypeIdentity> sysUserTypeIdentityList = sysUserTypeIdentityMapper.selectList(null);
        return sysUserTypeIdentityList.stream().map(
                list -> {
                    SysUserTypeIdentityVO vo = new SysUserTypeIdentityVO();
                    BeanUtils.copyProperties(list, vo);
                return vo;
        }).toList();
    }


    @Override
    public void updateIdentityMap(List<SysUserTypeIdentityVO> voList) {
        // 1. 清空原有所有映射关系
        sysUserTypeIdentityMapper.delete(null);

        // 2. 批量插入新的映射
        for (SysUserTypeIdentityVO vo : voList) {
            SysUserTypeIdentity entity = new SysUserTypeIdentity();
            BeanUtils.copyProperties(vo, entity);
            sysUserTypeIdentityMapper.insert(entity);
        }
    }
}
