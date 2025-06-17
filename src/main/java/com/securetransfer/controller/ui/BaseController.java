package com.securetransfer.controller.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public abstract class BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    protected ApplicationContext springContext;
    
    public void setSpringContext(ApplicationContext context) {
        this.springContext = context;
    }
    
    protected FXMLLoader createFxmlLoader(String fxmlPath) {
        logger.debug("Creating FXML loader for path: {}", fxmlPath);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        loader.setControllerFactory(springContext::getBean);
        return loader;
    }
    
    protected abstract void loadLoginScreen();
    
    protected abstract void loadRegistrationScreen();
} 