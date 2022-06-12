package com.greenjon902.gjms.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private static final String exampleServerStatus = """
            {
                "version": {
                    "name": "1.8.7",
                    "protocol": 47
                },
                "players": {
                    "max": 100,
                    "online": 5,
                    "sample": [
                        {
                            "name": "thinkofdeath",
                            "id": "4566e69f-c907-48ee-8d71-d7ba5aa00d20"
                        }
                    ]
                },
                "description": {
                    "text": "Hello world"
                }"
            }""";

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
                    System.out.println("Incoming connection from " + connection.getInetAddress());

                    InputStream inputStream = connection.getInputStream();
                    OutputStream outputStream = connection.getOutputStream();


                    int packetLength = DataTypes.decodeVarInt(inputStream);
                    int packetId = DataTypes.decodeVarInt(inputStream);

                    if (packetId == 0x00) {
                        int protocolVersion = DataTypes.decodeVarInt(inputStream);
                        inputStream.readNBytes(DataTypes.decodeVarInt(inputStream)); // We don't need the server address
                        inputStream.readNBytes(2); // We don't need the server port
                        int nextState = DataTypes.decodeVarInt(inputStream);

                        if (nextState == 1) { // Get server status
                            System.out.println("Connection wanted status");
                            DataTypes.writeVarInt(outputStream, exampleServerStatus.length());
                            outputStream.write(exampleServerStatus.getBytes(StandardCharsets.UTF_8));

                            inputStream.close();
                            outputStream.close();
                            connection.close();
                            System.out.println("Closed connection");
                        }
                    } else {
                        throw new RuntimeException("Unexpected packet with id " + packetId);
                    }

                } catch (Exception e) {
                    System.out.println("Failed to accept incoming connection - " + e.getMessage());
                }
            }
        }).start();
    }


}
