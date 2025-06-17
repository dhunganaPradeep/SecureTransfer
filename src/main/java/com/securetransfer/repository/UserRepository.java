package com.securetransfer.repository;

import com.securetransfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUserToken(String userToken);
    
    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();
    
    boolean existsByUsername(String username);
} 