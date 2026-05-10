package com.sc.qisi_system;

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

    // 项目启动后自动执行：插入管理员
    @PostConstruct
    public void insertAdmin() {
        try {

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

            System.out.println("✅ 管理员账号已自动插入：admin / 123456");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
