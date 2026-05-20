package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Student;
import com.example.attendancesystem.entity.User;
import com.example.attendancesystem.repository.StudentRepository;
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
    private StudentRepository studentRepository;

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

        if (user.getRole() == User.Role.STUDENT) {
            if (!studentRepository.existsByStudentId(user.getUsername())) {
                Student student = new Student();
                student.setStudentId(user.getUsername());
                student.setStudentName(user.getRealName());
                student.setClassName("待分配");
                student.setCreateTime(LocalDateTime.now());
                studentRepository.save(student);
            }
        }

        return "注册成功";
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
