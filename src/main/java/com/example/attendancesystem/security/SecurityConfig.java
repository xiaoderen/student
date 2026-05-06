package com.example.attendancesystem.security;

import com.example.attendancesystem.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            com.example.attendancesystem.entity.User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（因为使用 Basic Authentication）
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 注册接口无需认证
                        .requestMatchers("/user/register").permitAll()
                        // 学生相关接口：所有角色都可以访问
                        .requestMatchers("/student/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        // 课程相关接口：管理员和教师可以访问
                        .requestMatchers("/course/**").hasAnyRole("ADMIN", "TEACHER")
                        // 考勤相关接口：管理员和教师可以访问
                        .requestMatchers("/attendance/**").hasAnyRole("ADMIN", "TEACHER")
                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.realmName("Attendance System"))
                // 无状态会话
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
