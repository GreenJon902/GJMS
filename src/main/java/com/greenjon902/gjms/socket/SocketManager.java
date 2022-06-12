package com.greenjon902.gjms.socket;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Distributes all incoming and outgoing connections
 */
public class SocketManager {
    private final int port;
    private final ServerSocket serverSocket;

    public SocketManager(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Starts the thread that listens to incoming connections
     */
    public void openSocket() {

    }
}
