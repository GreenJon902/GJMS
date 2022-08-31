package com.greenjon902.gjms.connection.prePlay;

import com.greenjon902.gjms.GJMS;
import com.greenjon902.gjms.common.*;
import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.play.SocketPlayerImpl;
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
public class PrePlayConnectionHandler implements ConnectionHandler {
    private final List<PrePlayConnection> connections = Collections.synchronizedList(new ArrayList<>());

    private FirstWorldGetter firstWorldGetter;
    private ServerListPingStatusGetter serverListPingStatusGetter;

    public PrePlayConnectionHandler(FirstWorldGetter firstWorldGetter,
                                    ServerListPingStatusGetter serverListPingStatusGetter) {
        this.firstWorldGetter = firstWorldGetter;
        this.serverListPingStatusGetter = serverListPingStatusGetter;
    }

    /**
     * Adds a connection that will need to be handled. Has to be an instance of {@link PrePlayConnection}
     *
     * @param connection The connection to be added
     */
    public void addConnection(@NotNull Connection connection) {
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
                        tryHandleNextPacketFrom(connection);
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
     * Handles the next packet from a {@link PrePlayConnection} if there is a packet to be read.
     *
     * @param connection The connection where the packet is coming from
     */
    private void tryHandleNextPacketFrom(@NotNull PrePlayConnection connection) throws IOException {
        ServerboundPacket packet = connection.receive();
        if (packet == null) {
            return;
        }

        switch (connection.getPrePlayConnectionState()) {
            case HANDSHAKE -> {
                if (!(packet instanceof HandshakePacket handshakePacket)) {
                    throw new RuntimeException("Clients in the HANDSHAKE state can only send HandshakePacket");
                }
                connection.updatePacketAdaptor(handshakePacket.nextState, handshakePacket.protocolVersion);
            }
            case STATUS -> {
                if (packet instanceof StatusRequest) { // TODO: Get actual data
                    ClientboundPacket response = new StatusResponse(serverListPingStatusGetter,
                            connection.getProtocolVersion());
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

                        Player player = new SocketPlayerImpl(
                                UUID.nameUUIDFromBytes(("OfflinePlayer:" + connection.name).getBytes()),
                                connection.name,
                                connection.getSocket(),
                                connection.getProtocolVersion());

                        GJMS.worldHandler.get(firstWorldGetter.getWorld(player.getPlayerId())).addPlayer(player);

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
