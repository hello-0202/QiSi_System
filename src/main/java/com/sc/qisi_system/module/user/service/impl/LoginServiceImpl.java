package com.sc.qisi_system.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.common.exception.BusinessException;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.JwtTokenProvider;
import com.sc.qisi_system.module.admin.entity.SysUserTypeIdentity;
import com.sc.qisi_system.module.admin.service.SysUserTypeIdentityService;
import com.sc.qisi_system.module.user.dto.LoginDTO;
import com.sc.qisi_system.module.user.dto.LogoutDTO;
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


/**
 * 登录服务实现类
 */
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


    /**
     * 用户登录
     */
    @Override
    public LoginUserVO login(LoginDTO loginDTO) {
        // 1. 校验验证码
        captchaService.checkCaptcha(loginDTO.getCaptchaKey(), loginDTO.getCaptchaCode());

        // 2. 根据账号类型构建查询条件（手机号/用户名）
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (loginDTO.getUsername().matches("^1[3-9]\\d{9}$")) {
            queryWrapper.eq("phone", loginDTO.getUsername());
        } else {
            queryWrapper.eq("username", loginDTO.getUsername());
        }

        // 3. 查询用户基础信息
        queryWrapper.select("id","username","password","user_type","status");
        SysUser sysUser = userMapper.selectOne(queryWrapper);

        // 4. 校验用户是否存在
        if(sysUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 5. 校验用户状态是否锁定
        if(!sysUser.getStatus()){
            throw new BusinessException(ResultCode.USER_LOCKED);
        }

        // 6. 校验密码是否正确
        if(!passwordEncoder.matches(loginDTO.getPassword(), sysUser.getPassword())){
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 7. 生成Token
        String accessToken = jwtTokenProvider.generateAccessToken(sysUser.getId(),sysUser.getUsername(),sysUser.getUserType());
        String refreshToken = redisService.generateRefreshToken(sysUser.getId());

        // 8. 封装登录返回信息
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setAccessToken(accessToken);
        loginUserVO.setRefreshToken(refreshToken);
        loginUserVO.setUserId(sysUser.getId());
        loginUserVO.setUsername(sysUser.getUsername());
        loginUserVO.setUserType(sysUser.getUserType());
        loginUserVO.setUserTypeDesc( UserTypeEnum.getDescDescByCode(sysUser.getUserType()));

        // 9. 查询用户身份并加载菜单路由
        LambdaQueryWrapper<SysUserTypeIdentity> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1
                .eq(SysUserTypeIdentity::getUserType, sysUser.getUserType());
        SysUserTypeIdentity sysUserTypeIdentity = sysUserTypeIdentityService.getOne(queryWrapper1);
        loginUserVO.setMenuRoute(sysUserTypeIdentityService.getMenuRouteList(sysUserTypeIdentity.getIdentityId()));

        return loginUserVO;
    }


    /**
     * 用户登出
     */
    @Override
    public void logout(LogoutDTO logoutDTO) {
        // 1. 清理Redis登录状态
        redisService.logout(logoutDTO);
        // 2. 广播在线用户列表更新
        webSocketService.broadcastOnlineUserList();
    }
}