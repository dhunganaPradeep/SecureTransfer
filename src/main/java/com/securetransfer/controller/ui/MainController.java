package com.securetransfer.controller.ui;

import com.securetransfer.model.FileTransfer;
import com.securetransfer.service.AuthenticationService;
import com.securetransfer.service.FileTransferService;
import com.securetransfer.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class MainController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    @FXML
    private TableView<FileTransfer> fileTable;
    @FXML
    private TableColumn<FileTransfer, String> fileNameColumn;
    @FXML
    private TableColumn<FileTransfer, String> fileSizeColumn;
    @FXML
    private TableColumn<FileTransfer, String> statusColumn;
    @FXML
    private ProgressBar transferProgress;
    @FXML
    private Label statusLabel;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button transferButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label welcomeLabel;

    private final AuthenticationService authenticationService;
    private final FileTransferService fileTransferService;
    private final ObservableList<FileTransfer> fileList;
    @Autowired
    private UserService userService;

    @Autowired
    public MainController(AuthenticationService authenticationService, FileTransferService fileTransferService) {
        this.authenticationService = authenticationService;
        this.fileTransferService = fileTransferService;
        this.fileList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        fileTable.setItems(fileList);
        welcomeLabel.setText("Welcome to Secure Transfer!");
    }

    private void setupTableColumns() {
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSizeColumn.setCellValueFactory(cellData -> {
            FileTransfer file = cellData.getValue();
            return new SimpleStringProperty(formatFileSize(file.getLength()));
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    public void handleSendFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Send");
        File selectedFile = fileChooser.showOpenDialog(selectFileButton.getScene().getWindow());
        
        if (selectedFile != null) {
            fileList.add(new FileTransfer(selectedFile));
            statusLabel.setText("File selected: " + selectedFile.getName());
        }
    }

    @FXML
    public void handleTransfer() {
        FileTransfer selectedFileTransfer = fileTable.getSelectionModel().getSelectedItem();
        if (selectedFileTransfer == null) {
            statusLabel.setText("Please select a file to transfer");
            return;
        }

        File selectedFile = selectedFileTransfer.getFile();
        transferProgress.setProgress(0);
        statusLabel.setText("Transferring file: " + selectedFile.getName());
        selectedFileTransfer.setStatus("Transferring");

        CompletableFuture<Void> transferFuture = fileTransferService.transferFile(selectedFile, "peer1");
        transferFuture.thenRun(() -> {
            statusLabel.setText("Transfer completed: " + selectedFile.getName());
            transferProgress.setProgress(1.0);
            selectedFileTransfer.setStatus("Completed");
        }).exceptionally(throwable -> {
            statusLabel.setText("Transfer failed: " + throwable.getMessage());
            transferProgress.setProgress(0);
            selectedFileTransfer.setStatus("Failed");
            return null;
        });
    }

    @FXML
    private void handleLogout() {
        try {
            logger.debug("Loading login screen");
            FXMLLoader loader = createFxmlLoader("/fxml/login.fxml");
            Parent root = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setSpringContext(springContext);
            }
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            logger.error("Failed to load login screen", e);
        }
    }

    @Override
    protected void loadLoginScreen() {
        try {
            logger.debug("Loading login screen");
            FXMLLoader loader = createFxmlLoader("/fxml/login.fxml");
            Parent root = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setSpringContext(springContext);
            }
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            logger.error("Failed to load login screen", e);
        }
    }

    @Override
    protected void loadRegistrationScreen() {
        try {
            logger.debug("Loading registration screen");
            FXMLLoader loader = createFxmlLoader("/fxml/registration.fxml");
            Parent root = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setSpringContext(springContext);
            }
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            logger.error("Failed to load registration screen", e);
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
} 