package com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound;

import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;

/**
 * A packet sent from the client that contains information about the version of the client and why it's connecting.
 */
public class HandshakePacket implements ServerboundPacket {
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

    /**
     * The packet id is the way that minecraft clients are able to understand what each packet is and what it will do.
     *
     * @return The packet id of this packet
     */
    @Override
    public int getPacketId() {
        return 0;
    }
}
