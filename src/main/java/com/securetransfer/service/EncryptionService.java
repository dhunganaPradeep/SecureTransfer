package com.securetransfer.service;

/**
 * Service interface for handling encryption and password hashing operations.
 */
public interface EncryptionService {
    /**
     * Hashes a password using a secure algorithm.
     * @param password The plain text password to hash
     * @return The hashed password
     */
    String hashPassword(String password);

    /**
     * Verifies if a plain text password matches a hashed password.
     * @param rawPassword The plain text password to verify
     * @param hashedPassword The hashed password to compare against
     * @return true if the passwords match, false otherwise
     */
    boolean verifyPassword(String rawPassword, String hashedPassword);

    /**
     * Encrypts data using AES encryption.
     * @param data The data to encrypt
     * @return The encrypted data as a Base64 encoded string
     */
    String encrypt(String data);

    /**
     * Decrypts previously encrypted data.
     * @param encryptedData The encrypted data as a Base64 encoded string
     * @return The decrypted data
     */
    String decrypt(String encryptedData);
} 