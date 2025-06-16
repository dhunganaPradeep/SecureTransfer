package com.securetransfer.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class FileTransfer {
    private final File file;
    private final StringProperty status;

    public FileTransfer(File file) {
        this.file = file;
        this.status = new SimpleStringProperty("Pending");
    }

    public String getName() {
        return file.getName();
    }

    public long getLength() {
        return file.length();
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public File getFile() {
        return file;
    }
} 