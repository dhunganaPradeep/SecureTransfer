package com.securetransfer.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ProgressBar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferItem {
    private final StringProperty fileName;
    private final StringProperty size;
    private final ObjectProperty<ProgressBar> progress;
    private final StringProperty status;
    private final StringProperty date;

    public TransferItem(String fileName, String size, ProgressBar progress, String status, String date) {
        this.fileName = new SimpleStringProperty(fileName);
        this.size = new SimpleStringProperty(size);
        this.progress = new SimpleObjectProperty<>(progress);
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public ObjectProperty<ProgressBar> progressProperty() {
        return progress;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setProgress(double value) {
        progress.get().setProgress(value);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
} 