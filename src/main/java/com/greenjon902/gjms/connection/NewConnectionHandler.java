package com.greenjon902.gjms.connection;

import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandlerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Accepts any incoming connections and gives them to {@link PrePlayConnectionHandlerImpl}
 */
public class NewConnectionHandler {
    private final int port;
    private final ServerSocket serverSocket;
    private boolean cracked;
    private ConnectionHandler forwardTo;

    public NewConnectionHandler(int port, boolean cracked, ConnectionHandler forwardTo) {
        this.cracked = cracked;
        this.forwardTo = forwardTo;
        try {
            serverSocket = new ServerSocket(port);
            this.port = serverSocket.getLocalPort(); // May be allocated by java if port argument was 0
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NewConnectionHandler(boolean cracked, ConnectionHandler forwardTo) {
        this(0, cracked, forwardTo); // Start server on a free port that's allocated by java
    }

    public int getPort() {
        return port;
    }

    /**
     * Starts the thread that listens to incoming connections.
     */
    public void startNewHandler() {
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket connection = serverSocket.accept();
                    System.out.println("Incoming connection from " + connection.getInetAddress() + ":" + connection.getPort());

                    PrePlayConnection prePlayConnection = new PrePlayConnection(connection, cracked);
                        forwardTo.addConnection(prePlayConnection);

                } catch (Exception e) {
                    System.out.println("Failed to accept incoming connection - " + e.getMessage());
                }
            }
        }, "NewConnectionHandler").start();
    }


}
