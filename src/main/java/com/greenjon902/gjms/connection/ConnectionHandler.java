package com.greenjon902.gjms.connection;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionHandler {
    private static final ArrayList<PlayerConnection> playerConnections = new ArrayList();

    public static void addConnection(PlayerConnection playerConnection) {
        playerConnections.add(playerConnection);
    }

    public static void startManager() {
        new Thread(() -> {
            while (true) {
                for (PlayerConnection playerConnection : playerConnections) {
                    try {
                        if (playerConnection.inputStream.available() != 0) {
                            handleNextPacketFrom(playerConnection);
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to handle packet from " + playerConnection.ip);
                    }
                }
            }
        }).start();
    }

    private static void handleNextPacketFrom(PlayerConnection playerConnection) {
        switch (playerConnection.getConnectionState()) {
            case HANDSHAKE:
                break;
            default:
                throw new RuntimeException("Only ConnectionState.HANDSHAKE has been implemented");
        }
    }
}
