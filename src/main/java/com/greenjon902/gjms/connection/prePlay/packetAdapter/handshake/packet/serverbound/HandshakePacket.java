package com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;

/**
 * A packet sent from the client that contains information about the version of the client ({@link #protocolVersion})
 * and why it's connecting ({@link #nextState}). This also includes the address the client connected to
 * ({@link #serverAddress}, {@link #port}).
 */
public class HandshakePacket extends ServerboundPacket {
    public final int protocolVersion;
    public final String serverAddress;
    public final int port;
    public final PrePlayConnectionState nextState;

    public HandshakePacket(int protocolVersion, String serverAddress, int port, PrePlayConnectionState nextState) {
        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.port = port;
        this.nextState = nextState;
    }

    public HandshakePacket(int protocolVersion, String serverAddress, int port, int nextState) {
        PrePlayConnectionState prePlayConnectionState = switch (nextState) {
            case 1 -> PrePlayConnectionState.STATUS;
            case 2 -> PrePlayConnectionState.LOGIN;
            default -> throw new IllegalArgumentException("Argument nextState can be between 1 and 2");
        };

        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.port = port;
        this.nextState = prePlayConnectionState;
    }

    @Override
    public int getPacketId() {
        return 0;
    }

    @Override
    public String toString() {
        return "HandshakePacket{" +
                "protocolVersion=" + protocolVersion +
                ", serverAddress='" + serverAddress + '\'' +
                ", port=" + port +
                ", nextState=" + nextState +
                '}';
    }
}
