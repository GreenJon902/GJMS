package com.greenjon902.gjms.connection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles all currently open connections. It is given connections by the {@link NewConnectionHandler}.
 */
public class ConnectionHandler {
    private static final ArrayList<PlayerConnection> playerConnections = new ArrayList<>();

    /**
     * Adds a connection that will need to be handled.
     *
     * @param playerConnection The player connection to be added
     */
    public static void addConnection(PlayerConnection playerConnection) {
        playerConnections.add(playerConnection);
    }

    /**
     * Starts a new thread, these constantly loop through the current open connections and when a connection sends a
     * packet, it will be handled by {@link #handleNextPacketFrom(PlayerConnection)}. You can have multiple running at
     * one to improve speed when there is a large number of connections.
     */
    public static void startNewHandler() {
        new Thread(() -> {
            while (true) {
                for (PlayerConnection playerConnection : playerConnections) {
                    try {
                        if (playerConnection.inputStream.available() != 0) {  // Only handle packets when there is a
                                                                              // packet to be handled
                            handleNextPacketFrom(playerConnection);
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to handle packet from " + playerConnection.ip);
                    }
                }
            }
        }).start();
    }

    /**
     * Handles the next packet from a {@link PlayerConnection}.
     *
     * @param playerConnection The player connection where the packet is coming from
     */
    private static void handleNextPacketFrom(PlayerConnection playerConnection) {
        switch (playerConnection.getConnectionState()) {  // TODO: Add other ConnectionStates
            case HANDSHAKE:
                break;
            default:
                throw new RuntimeException("Only ConnectionState.HANDSHAKE has been implemented");
        }
    }
}
