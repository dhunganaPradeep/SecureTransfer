package com.securetransfer.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileTransferService {
    /**
     * Start a file transfer to a peer
     * @param file The file to transfer
     * @param peerId The ID of the peer to transfer to
     * @return A CompletableFuture that completes when the transfer is done
     */
    CompletableFuture<Void> transferFile(File file, String peerId);

    /**
     * Start receiving a file from a peer
     * @param peerId The ID of the peer sending the file
     * @param fileName The name of the file being received
     * @param fileSize The size of the file in bytes
     * @return A CompletableFuture that completes when the file is received
     */
    CompletableFuture<File> receiveFile(String peerId, String fileName, long fileSize);

    /**
     * Cancel an ongoing file transfer
     * @param transferId The ID of the transfer to cancel
     */
    void cancelTransfer(String transferId);

    /**
     * Get a list of active transfers
     * @return List of active transfer IDs
     */
    List<String> getActiveTransfers();

    /**
     * Get the progress of a specific transfer
     * @param transferId The ID of the transfer
     * @return Progress as a value between 0 and 1
     */
    double getTransferProgress(String transferId);
} 