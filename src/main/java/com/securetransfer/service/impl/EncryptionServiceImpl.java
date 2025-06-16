package com.securetransfer.service.impl;

import com.securetransfer.service.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final byte[] encryptionKey;
    private static final String ALGORITHM = "AES";

    public EncryptionServiceImpl(@Value("${app.encryption.key}") String encryptionKey) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        try {
            // Decode the Base64 encoded key and ensure it's 32 bytes for AES-256
            byte[] decodedKey = Base64.getDecoder().decode(encryptionKey.trim());
            if (decodedKey.length != 32) {
                throw new IllegalArgumentException("Encryption key must be 32 bytes for AES-256");
            }
            this.encryptionKey = decodedKey;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid encryption key format: " + e.getMessage());
        }
    }

    @Override
    public String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public String encrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(encryptionKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data: " + e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(encryptionKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data: " + e.getMessage(), e);
        }
    }
} 