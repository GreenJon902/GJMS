package com.greenjon902.gjms.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket connection = serverSocket.accept();
                    System.out.println("Incoming connection from " + connection.getInetAddress() + ":" + connection.getPort());



                } catch (Exception e) {
                    System.out.println("Failed to accept incoming connection - " + e.getMessage());
                }
            }
        }).start();
    }


}
