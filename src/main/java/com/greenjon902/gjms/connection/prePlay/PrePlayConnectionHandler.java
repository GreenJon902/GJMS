package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.Packet;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound.HandshakePacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles connections from clients who are not in the play state.It is given connections by the
 * {@link NewConnectionHandler}.
 */
public class PrePlayConnectionHandler {
    private static final List<PrePlayConnection> connections = Collections.synchronizedList(new ArrayList<PrePlayConnection>());

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
            int i = 0;
            while (true) {
                if (connections.size() != 0) {
                    PrePlayConnection connection = connections.get(i);
                    i++;
                    i %= connections.size();

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
                Packet packet = connection.getPacketAdapter().getFirstPacket(connection);
                if (!(packet instanceof HandshakePacket handshakePacket)) {
                    throw new RuntimeException("Clients in the HANDSHAKE state can only send HandshakePacket");
                }
                connection.setProtocolVersion(handshakePacket.protocolVersion);
                connection.setPrePlayConnectionState(handshakePacket.nextState);
                break;

            default:
                throw new RuntimeException("Only PrePlayConnectionState.HANDSHAKE has been implemented");
        }
    }

    /**
     * Gets all open {@link PrePlayConnection}s, used for testing.
     *
     * @return All open connections
     */
    public static PrePlayConnection[] getConnections() {
        return connections.toArray(PrePlayConnection[]::new);
    }
}
