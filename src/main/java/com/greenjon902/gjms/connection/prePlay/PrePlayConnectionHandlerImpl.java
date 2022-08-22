package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.common.PlayerConnection;
import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound.HandshakePacket;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound.LoginSuccess;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.serverbound.LoginStart;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.PingResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.StatusResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.PingRequest;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.StatusRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Handles connections from clients who are not in the play state. It is given connections by the
 * {@link com.greenjon902.gjms.connection.NewConnectionHandler}.
 */
public class PrePlayConnectionHandlerImpl implements ConnectionHandler {
    private final List<PrePlayConnection> connections = Collections.synchronizedList(new ArrayList<>());

    /**
     * Adds a connection that will need to be handled. Has to be an instance of {@link PrePlayConnection}
     *
     * @param connection The connection to be added
     */
    public void addConnection(@NotNull PlayerConnection connection) {
        if (!(connection instanceof PrePlayConnection)) throw new IllegalArgumentException("PrePlayConnectionHandlerImpl#" +
                "addConnection can only take instances of PrePlayConnection!");
        connections.add((PrePlayConnection) connection);
    }

    public void startNewHandler() {
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
                        System.err.println("Failed to handle packet from " + connection.ip + " - " + e.getMessage());
                        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                            System.err.println(stackTraceElement.toString());
                        }
                    }
                }
            }
        }, "PrePlayConnectionHandler").start();
    }

    /**
     * Handles the next packet from a {@link PrePlayConnection}.
     *
     * @param connection The connection where the packet is coming from
     */
    private void handleNextPacketFrom(@NotNull PrePlayConnection connection) throws IOException {
        ServerboundPacket packet = connection.receive();

        switch (connection.getPrePlayConnectionState()) {
            case HANDSHAKE -> {
                if (!(packet instanceof HandshakePacket handshakePacket)) {
                    throw new RuntimeException("Clients in the HANDSHAKE state can only send HandshakePacket");
                }
                connection.updatePacketAdaptor(handshakePacket.nextState, handshakePacket.protocolVersion);
            }
            case STATUS -> {
                if (packet instanceof StatusRequest) { // TODO: Get actual data
                    ClientboundPacket response = new StatusResponse("GJMS", connection.getProtocolVersion(),
                            12, 3,
                            "[{\"name\":\"GreenJon\",\"id\":\"86f5d3d8-0d4b-4230-9852-77a40baf39bd\"}," +
                                    "{\"name\":\"AdminJon_\",\"id\":\"0f549ef4-000b-4a9a-8fd2-2c3e7044ea54\"}," +
                                    "{\"name\":\"Dream\",\"id\":\"ec70bcaf-702f-4bb8-b48d-276fa52a780c\"}]",
                            "{\"text\": \"Hello World!\"}", false);
                    connection.send(response);
                } else if (packet instanceof PingRequest pingRequest) {
                    ClientboundPacket response = new PingResponse(pingRequest.payload);
                    connection.send(response);
                } else {
                    throw new RuntimeException("Clients in the STATUS state can only send StatusRequest or PingRequest");
                }
            }
            case LOGIN -> {
                if (packet instanceof LoginStart loginStart) {
                    connection.name = loginStart.name;
                    if (loginStart.hasSignatureData()) {
                        connection.timestamp = loginStart.timestamp;
                        connection.publicKey = loginStart.publicKey;
                        connection.signature = loginStart.signature;
                    }

                    if (connection.cracked) {
                        LoginSuccess response = new LoginSuccess(
                                UUID.nameUUIDFromBytes(("OfflinePlayer:" + connection.name).getBytes()),
                                connection.name,
                                new LoginSuccess.Property[0]);
                        connection.send(response);

                    } else {
                        throw new RuntimeException("Non-cracked logging in has not yet been implemented");
                    }

                } else {
                    throw new RuntimeException("Clients in the Login state can only send LoginStart or " +
                            "EncryptionResponse");
                }
            }
            default -> throw new RuntimeException("Unknown PrePlayConnectionState");
        }
    }

    public PrePlayConnection[] getConnections() {
        return connections.toArray(PrePlayConnection[]::new);
    }
}