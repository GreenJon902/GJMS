package com.greenjon902.gjms.connection;

import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Accepts any incoming connections and gives them to {@link PrePlayConnectionHandler}
 */
public class NewConnectionHandler {
    private final int port;
    private final ServerSocket serverSocket;
    private boolean cracked;

    public NewConnectionHandler(int port, boolean cracked) {
        this.cracked = cracked;
        try {
            serverSocket = new ServerSocket(port);
            this.port = serverSocket.getLocalPort(); // May be allocated by java if port argument was 0
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NewConnectionHandler(boolean cracked) {
        this(0, cracked); // Start server on a free port that's allocated by java
    }

    public int getPort() {
        return port;
    }

    /**
     * Starts the thread that listens to incoming connections.
     */
    public void start() {
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket connection = serverSocket.accept();
                    System.out.println("Incoming connection from " + connection.getInetAddress() + ":" + connection.getPort());

                    PrePlayConnection prePlayConnection = new PrePlayConnection(connection, cracked);
                        PrePlayConnectionHandler.addConnection(prePlayConnection);

                } catch (Exception e) {
                    System.out.println("Failed to accept incoming connection - " + e.getMessage());
                }
            }
        }).start();
    }


}
