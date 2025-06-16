package com.securetransfer.service.impl;

import com.securetransfer.service.FileTransferService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FileTransferServiceImpl implements FileTransferService {
    private final Map<String, TransferState> activeTransfers = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Void> transferFile(File file, String peerId) {
        String transferId = generateTransferId(file, peerId);
        TransferState state = new TransferState(file, peerId);
        activeTransfers.put(transferId, state);

        return CompletableFuture.runAsync(() -> {
            try {
                // TODO: Implement actual file transfer logic
                // This is a placeholder that simulates a transfer
                for (int i = 0; i <= 100; i++) {
                    state.setProgress(i / 100.0);
                    Thread.sleep(50); // Simulate transfer time
                }
                activeTransfers.remove(transferId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Transfer interrupted", e);
            }
        });
    }

    @Override
    public CompletableFuture<File> receiveFile(String peerId, String fileName, long fileSize) {
        String transferId = generateTransferId(new File(fileName), peerId);
        TransferState state = new TransferState(new File(fileName), peerId);
        activeTransfers.put(transferId, state);

        return CompletableFuture.supplyAsync(() -> {
            try {
                // TODO: Implement actual file receive logic
                // This is a placeholder that simulates receiving
                for (int i = 0; i <= 100; i++) {
                    state.setProgress(i / 100.0);
                    Thread.sleep(50); // Simulate receive time
                }
                activeTransfers.remove(transferId);
                return new File(fileName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Receive interrupted", e);
            }
        });
    }

    @Override
    public void cancelTransfer(String transferId) {
        TransferState state = activeTransfers.get(transferId);
        if (state != null) {
            state.setCancelled(true);
            activeTransfers.remove(transferId);
        }
    }

    @Override
    public List<String> getActiveTransfers() {
        return List.copyOf(activeTransfers.keySet());
    }

    @Override
    public double getTransferProgress(String transferId) {
        TransferState state = activeTransfers.get(transferId);
        return state != null ? state.getProgress() : 0.0;
    }

    private String generateTransferId(File file, String peerId) {
        return file.getName() + "_" + peerId + "_" + System.currentTimeMillis();
    }

    private static class TransferState {
        private final File file;
        private final String peerId;
        private final AtomicReference<Double> progress = new AtomicReference<>(0.0);
        private volatile boolean cancelled = false;

        public TransferState(File file, String peerId) {
            this.file = file;
            this.peerId = peerId;
        }

        public void setProgress(double progress) {
            this.progress.set(progress);
        }

        public double getProgress() {
            return progress.get();
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

        public boolean isCancelled() {
            return cancelled;
        }
    }
} 