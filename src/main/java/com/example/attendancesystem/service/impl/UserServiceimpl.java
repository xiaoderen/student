 package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.User;
import com.example.attendancesystem.repository.UserRepository;
import com.example.attendancesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            return "用户名已存在";
        }

        if (user.getRole() == null) {
            return "角色不能为空";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setCreateTime(LocalDateTime.now());

        userRepository.save(user);
        return "注册成功";
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
