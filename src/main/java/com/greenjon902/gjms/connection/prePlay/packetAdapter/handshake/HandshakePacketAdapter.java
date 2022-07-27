package com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound.HandshakePacket;

import java.io.IOException;

/**
 * Contains the functionality for converting packets when the connection's {@link PrePlayConnectionState}
 * is {@link PrePlayConnectionState#HANDSHAKE}. There is no protocolVersion requirement as the server does not know the
 * connections protocolVersion yet!
 */
public class HandshakePacketAdapter extends PrePlayPacketAdapter {
    /**
     * Gets the first packet from a connection
     *
     * @param connection The connection where the packet is coming from
     */
    @Override
    public ServerboundPacket getFirstPacket(Connection connection) throws IOException {
        int packetLength = decodeFirstVarInt(connection);
        int packetId = decodeFirstVarInt(connection);

        switch (packetId) {
            case 0x00: // 00000000 - Handshake
                int protocolVersion = decodeFirstVarInt(connection);
                String serverAddress = decodeFirstString(connection);
                int port = decodeFirstUnsignedShort(connection);
                int nextState = decodeFirstVarInt(connection);
                return new HandshakePacket(protocolVersion, serverAddress, port, nextState);
            default:
                throw new RuntimeException("Unknown packet with ID " + packetId);
        }
    }

    @Override
    protected byte[] encodePacket(ClientboundPacket packet) throws IOException {
        throw new RuntimeException("HandshakePacketAdapter has no clientbound packets");
    }
}
