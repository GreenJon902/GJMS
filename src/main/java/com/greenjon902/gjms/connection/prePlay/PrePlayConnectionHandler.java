package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.connection.NewConnectionHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles connections from clients who are not in the play state.It is given connections by the
 * {@link NewConnectionHandler}.
 */
public class PrePlayConnectionHandler {
    private static final ArrayList<PrePlayConnection> connections = new ArrayList<>();

    /**
     * Adds a connection that will need to be handled.
     *
     * @param connection The connection to be added
     */
    public static void addConnection(@NotNull PrePlayConnection connection) {
        connections.add(connection);
    }

    /**
     * Starts a new thread, these constantly loop through the current open connections and when a connection sends a
     * packet, it will be handled by {@link #handleNextPacketFrom(PrePlayConnection)}. You can have multiple running at
     * one to improve speed when there is a large number of connections.
     */
    public static void startNewHandler() {
        new Thread(() -> {
            while (true) {
                for (PrePlayConnection connection : connections) {
                    try {
                        if (connection.inputStream.available() != 0) {  // Only handle packets when there is a
                                                                              // packet to be handled
                            handleNextPacketFrom(connection);
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to handle packet from " + connection.ip);
                    }
                }
            }
        }).start();
    }

    /**
     * Handles the next packet from a {@link PrePlayConnection}.
     *
     * @param connection The connection where the packet is coming from
     */
    private static void handleNextPacketFrom(@NotNull PrePlayConnection connection) throws IOException {
        switch (connection.getPrePlayConnectionState()) {  // TODO: Add other PrePlayConnectionStates
            case HANDSHAKE:
                System.out.println(connection.inputStream.read());
                break;

            default:
                throw new RuntimeException("Only PrePlayConnectionState.HANDSHAKE has been implemented");
        }
    }
}
