package com.securetransfer.service;

import com.securetransfer.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User authenticate(String username, String password);
    User register(String username, String password);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> getAllUsers();
    List<String> getAllUsernames();
    User getUserByUsername(String username);
} 