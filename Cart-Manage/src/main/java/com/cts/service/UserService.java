package com.cts.service;

import com.cts.entity.User;

import java.util.Optional;


public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User saveUser(User user);
    User updateUserProfile(String currentUsername, User updatedUser);
    
}
