package com.sc.qisi_system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sc.qisi_system.common.enums.UserTypeEnum;
import com.sc.qisi_system.module.user.entity.SysUser;
import com.sc.qisi_system.module.user.service.SysUserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
public class QiSiSystemApplication {


    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(QiSiSystemApplication.class, args);
    }


    @PostConstruct
    public void insertAdmin() {
        try {
            boolean exists = sysUserService.exists(
                    Wrappers.lambdaQuery(SysUser.class)
                            .eq(SysUser::getUsername, "admin")
            );

            if (exists) {
                log.info("✅ 管理员账号已初始化");
                return;
            }

            log.info("开始初始化管理员账号...");
            SysUser user = new SysUser();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setName("系统管理员");
            user.setUserType(UserTypeEnum.ADMIN.getCode());
            user.setPhone("11111111111");
            user.setEmail("admin@qisi.com");
            user.setAvatar(null);
            user.setStatus(true);

            sysUserService.save(user);
            log.info("✅ 管理员账号初始化成功");
            log.info("账号：admin");
            log.info("密码：123456");

        } catch (Exception e) {
            log.error("❌ 管理员账号初始化失败：", e);
        }
    }
}


