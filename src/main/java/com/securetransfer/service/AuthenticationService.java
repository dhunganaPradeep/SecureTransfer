package com.securetransfer.service;

import com.securetransfer.model.User;
import java.util.Optional;

public interface AuthenticationService {
    Optional<User> authenticate(String username, String password, String deviceId);
    User register(String username, String password, String deviceId);
    boolean validateToken(String token);
    void logout(String token);
} 