package com.greenjon902.gjms.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Accepts any incoming connections and gives them to {@link ConnectionHandler}
 */
public class NewConnectionHandler {
    private final int port;
    private final ServerSocket serverSocket;

    public NewConnectionHandler(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

                    PlayerConnection playerConnection = new PlayerConnection(connection);
                        ConnectionHandler.addConnection(playerConnection);

                } catch (Exception e) {
                    System.out.println("Failed to accept incoming connection - " + e.getMessage());
                }
            }
        }).start();
    }


}
