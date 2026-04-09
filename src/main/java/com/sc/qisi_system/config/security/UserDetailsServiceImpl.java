package com.sc.qisi_system.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
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

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        if (username.matches("^1[3-9]\\d{9}$")) {
            wrapper.eq("phone", username);
        } else {
            wrapper.eq("username", username);
        }

        SysUser sysUser = sysUserMapper.selectOne(wrapper);

        if (sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (!sysUser.getStatus()) {
            throw new BusinessException(ResultCode.USER_LOCKED);
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(UserTypeEnum.getNameByCode(sysUser.getUserType()))
        );

        return new org.springframework.security.core.userdetails.User(
                sysUser.getUsername(),
                sysUser.getPassword(),
                authorities
        );

    }
}
