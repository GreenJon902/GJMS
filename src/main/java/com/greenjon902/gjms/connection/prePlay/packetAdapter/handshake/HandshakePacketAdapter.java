package com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake;

import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.Packet;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnection;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.handshake.packet.serverbound.HandshakePacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Contains the functionality for converting packets when the client's
 * {@link PrePlayConnectionState} is {@link PrePlayConnectionState#HANDSHAKE}.
 */
public class HandshakePacketAdapter extends PrePlayPacketAdapter {
    static final PacketAdapter instance = new HandshakePacketAdapter();

    /**
     * Gets the usable instance of the {@link PacketAdapter}.
     *
     * @return The instance of the packet adapter
     */
    public static @NotNull PacketAdapter getInstance(){
        return instance;
    }

    /**
     * Gets the first packet from a connection
     *
     * @param connection The connection where the packet is coming from
     */
    @Override
    public Packet getFirstPacket(Connection connection) throws IOException {
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
}
