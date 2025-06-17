package com.securetransfer;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.securetransfer")
@EntityScan("com.securetransfer.model")
@EnableJpaRepositories("com.securetransfer.repository")
public class SecureTransferApplication {

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }
} 