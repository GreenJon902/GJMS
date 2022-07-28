package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound.HandshakePacket;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.PingResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.StatusResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.PingRequest;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.StatusRequest;
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
    private static final List<PrePlayConnection> connections = Collections.synchronizedList(new ArrayList<>());

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
                    } catch (Exception e) {
                        System.out.println("Failed to handle packet from " + connection.ip + " - " + e.getMessage());
                        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                            System.out.println(stackTraceElement.toString());
                        }
                        System.out.println();
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
        ServerboundPacket packet = connection.getPacketAdapter().getFirstPacket(connection);
        // TODO: Add other PrePlayConnectionStates
        switch (connection.getPrePlayConnectionState()) {
            case HANDSHAKE -> {
                if (!(packet instanceof HandshakePacket handshakePacket)) {
                    throw new RuntimeException("Clients in the HANDSHAKE state can only send HandshakePacket");
                }
                connection.updatePacketAdaptor(handshakePacket.nextState, handshakePacket.protocolVersion);
            }
            case STATUS -> {
                if (packet instanceof StatusRequest) {
                    ClientboundPacket response = new StatusResponse("1.19", connection.getProtocolVersion(),
                            12, 3,
                            "[{\"name\":\"GreenJon\",\"id\":\"86f5d3d8-0d4b-4230-9852-77a40baf39bd\"}," +
                                    "{\"name\":\"AdminJon_\",\"id\":\"0f549ef4-000b-4a9a-8fd2-2c3e7044ea54\"}," +
                                    "{\"name\":\"Dream\",\"id\":\"ec70bcaf-702f-4bb8-b48d-276fa52a780c\"}]",
                            "{\"text\": \"Hello World!\"}", false);
                    connection.getPacketAdapter().sendPacket(response, connection);
                } else if (packet instanceof PingRequest pingRequest) {
                    ClientboundPacket response = new PingResponse(pingRequest.payload);
                    connection.getPacketAdapter().sendPacket(response, connection);
                } else {
                    throw new RuntimeException("Clients in the STATUS state can only send StatusRequest or PingRequest");
                }
            }
            default -> throw new RuntimeException("Only PrePlayConnectionState.HANDSHAKE has been implemented");
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
