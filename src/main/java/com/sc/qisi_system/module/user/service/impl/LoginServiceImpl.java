package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.JwtTokenProvider;
import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.service.SysUserTypeIdentityService;
import com.sc.qisi_system.module.user.dto.LoginRequest;
import com.sc.qisi_system.module.user.dto.LogoutRequest;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.mapper.SysUserMapper;
import com.sc.qisi_system.module.user.service.CaptchaService;
import com.sc.qisi_system.module.user.service.LoginService;
import com.sc.qisi_system.module.user.service.RedisService;
import com.sc.qisi_system.module.user.vo.LoginUserVO;
import com.sc.qisi_system.module.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {


    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SysUserTypeIdentityService sysUserTypeIdentityService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CaptchaService captchaService;
    private final RedisService redisService;
    private final WebSocketService webSocketService;


    @Override
    public LoginUserVO login(LoginRequest loginRequest) {

        captchaService.checkCaptcha(loginRequest.getCaptchaKey(),loginRequest.getCaptchaCode());

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (loginRequest.getUsername().matches("^1[3-9]\\d{9}$")) {
            queryWrapper.eq("phone", loginRequest.getUsername());
        } else {
            queryWrapper.eq("username", loginRequest.getUsername());
        }

        queryWrapper.select("id","username","password","user_type","status");
        SysUser sysUser = userMapper.selectOne(queryWrapper);

        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if(!sysUser.getStatus()){
            throw new BusinessException(ResultCode.USER_LOCKED);
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), sysUser.getPassword())){
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(sysUser.getId(),sysUser.getUsername(),sysUser.getUserType());
        String refreshToken = redisService.generateRefreshToken(sysUser.getId());

        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setAccessToken(accessToken);
        loginUserVO.setRefreshToken(refreshToken);
        loginUserVO.setUserId(sysUser.getId());
        loginUserVO.setUsername(sysUser.getUsername());
        loginUserVO.setUserType(sysUser.getUserType());
        loginUserVO.setUserTypeDesc( UserTypeEnum.getDescDescByCode(sysUser.getUserType()));

        LambdaQueryWrapper<SysUserTypeIdentity> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1
                .eq(SysUserTypeIdentity::getUserType, sysUser.getUserType());
        SysUserTypeIdentity sysUserTypeIdentity = sysUserTypeIdentityService.getOne(queryWrapper1);
        loginUserVO.setMenuRoute(sysUserTypeIdentityService.getMenuRouteList(sysUserTypeIdentity.getIdentityId()));

        return loginUserVO;
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        redisService.logout(logoutRequest);
        webSocketService.broadcastOnlineUserList();
    }
}
