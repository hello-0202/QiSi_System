package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.config.security.LoginUser;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * Spring Security 用户详情服务实现类
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final SysUserMapper sysUserMapper;


    /**
     * 根据用户名/手机号加载用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        // 1. 判断是手机号还是用户名
        if (username.matches("^1[3-9]\\d{9}$")) {
            wrapper.eq("phone", username);
        } else {
            wrapper.eq("username", username);
        }

        // 2. 查询用户信息
        SysUser sysUser = sysUserMapper.selectOne(wrapper);

        // 3. 用户不存在
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 4. 用户被锁定
        if (!sysUser.getStatus()) {
            throw new BusinessException(ResultCode.USER_LOCKED);
        }

        // 5. 设置用户权限（根据用户类型）
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(UserTypeEnum.getByCode(sysUser.getUserType()).getAuthority())
        );

        // 6. 封装成 LoginUser 返回给 Security
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(sysUser.getUsername());
        loginUser.setPassword(sysUser.getPassword());
        loginUser.setAuthorities(authorities);
        loginUser.setUserId(sysUser.getId());

        return loginUser;
    }
}