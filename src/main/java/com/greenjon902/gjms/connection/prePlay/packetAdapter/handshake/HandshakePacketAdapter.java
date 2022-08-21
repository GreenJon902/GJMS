package com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound.HandshakePacket;

import java.io.IOException;
import java.io.InputStream;

/**
 * Contains the functionality for converting packets when the connection's {@link PrePlayConnectionState}
 * is {@link PrePlayConnectionState#HANDSHAKE}. There is no protocolVersion requirement as the server does not know the
 * connections protocolVersion yet!
 */
public class HandshakePacketAdapter extends PrePlayPacketAdapter {
    @Override
    public ServerboundPacket decodePacket(InputStream inputStream) throws IOException {
        int packetId = decodeFirstVarInt(inputStream);

        if (packetId == 0x00) { // 00000000 - Handshake
            int protocolVersion = decodeFirstVarInt(inputStream);
            String serverAddress = decodeFirstString(inputStream);
            int port = decodeFirstUnsignedShort(inputStream);
            int nextState = decodeFirstVarInt(inputStream);
            return new HandshakePacket(protocolVersion, serverAddress, port, nextState);
        }
        throw new RuntimeException("Unknown packet with ID " + packetId);
    }

    @Override
    protected byte[] encodePacket(ClientboundPacket packet) {
        throw new RuntimeException("HandshakePacketAdapter has no clientbound packets");
    }
}
