package com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound;

import com.greenjon902.gjms.connection.Packet;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;

/**
 * A packet sent from the client that contains information about the version of the client and why it's connecting.
 */
public class HandshakePacket implements Packet {
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
        PrePlayConnectionState prePlayConnectionState;
        switch (nextState) {
            case 1:
                prePlayConnectionState = PrePlayConnectionState.STATUS;
                break;
            case 2:
                prePlayConnectionState = PrePlayConnectionState.LOGIN;
                break;
            default:
                throw new IllegalArgumentException("Argument nextState can be between 1 and 2");
        }

        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.port = port;
        this.nextState = prePlayConnectionState;
    }
}
