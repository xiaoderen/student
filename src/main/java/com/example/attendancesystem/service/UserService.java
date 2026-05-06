 package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.User;

public interface UserService {
    String registerUser(User user);

    User getUserByUsername(String username);
}
