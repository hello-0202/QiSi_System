package com.sc.qisi_system.config.security;

import com.sc.qisi_system.config.security.filter.JwtAuthenticationFilter;
import com.sc.qisi_system.config.cors.CorsConfig;
import com.sc.qisi_system.config.security.handler.SecurityHandlerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
// 开启方法级权限控制，如 @PreAuthorize("hasRole('ADMIN')")
@EnableMethodSecurity(proxyTargetClass = true)
@RequiredArgsConstructor // 生成构造器注入所需的过滤器
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final SecurityHandlerConfig securityHandlerConfig;

    private final CorsConfig corsConfig;

    /**
     * 核心：配置 Security 过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. CSRF 防护：JWT 无状态，需禁用
                .csrf(AbstractHttpConfigurer::disable)
                // 2. CORS 配置：启用 CORS 并应用规则
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                // 3. 会话管理：使用 JWT，彻底抛弃 Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 4. 请求授权：定义路径访问策略
                .authorizeHttpRequests(auth -> auth
                        // 放行 Swagger、登录、注册等公开接口
                        .requestMatchers("/api/user/**", "/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()
                        // ✅ 在这里加一行：放行 WebSocket 连接地址
//                        .requestMatchers("/ws/**").permitAll()
                        // 对预检请求放行，这是解决 CORS 问题的关键
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 其他所有请求都需认证
                        .anyRequest().authenticated()
                )
                // 5. 异常处理：注册自定义认证/授权处理器
                .exceptionHandling(exception -> exception
                        // 处理 401 (未认证/Token 无效)
                        .authenticationEntryPoint(securityHandlerConfig.customAuthenticationEntryPoint())
                        // 处理 403 (已认证但无权限)
                        .accessDeniedHandler(securityHandlerConfig.customAccessDeniedHandler())
                )
                // 6. 添加过滤器：将自定义 JWT 过滤器置于 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/ws/**");
    }


}