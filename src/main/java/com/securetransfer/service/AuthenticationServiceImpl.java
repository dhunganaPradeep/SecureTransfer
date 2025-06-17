package com.securetransfer.service;

import com.securetransfer.model.User;
import com.securetransfer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> authenticate(String username, String password, String deviceId) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(user -> {
                    user.setDeviceId(deviceId);
                    user.setLastLogin(LocalDateTime.now());
                    return userRepository.save(user);
                });
    }

    @Override
    public User register(String username, String password, String deviceId) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setDeviceId(deviceId);
        user.setLastLogin(LocalDateTime.now());
        user.setUserToken(UUID.randomUUID().toString());
        user.setActive(true);

        return userRepository.save(user);
    }

    @Override
    public boolean validateToken(String token) {
        return userRepository.findByUserToken(token)
                .map(User::isActive)
                .orElse(false);
    }

    @Override
    public void logout(String token) {
        userRepository.findByUserToken(token)
                .ifPresent(user -> {
                    user.setUserToken(null);
                    userRepository.save(user);
                });
    }
} 