package com.greenjon902.gjms.connection.prePlay.packetAdapter.login;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.serverbound.EncryptionResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.serverbound.LoginStart;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.PingResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.StatusResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.PingRequest;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.StatusRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Contains the functionality for converting packets when the connection's {@link PrePlayConnectionState} is
 * {@link PrePlayConnectionState#LOGIN} and the protocolVersion is between 391 and 759.
 */
public class LoginPacketAdapter391to759 extends PrePlayPacketAdapter {
    /**
     * Gets the first packet from a connection
     *
     * @param connection The connection where the packet is coming from
     */
    @Override
    public ServerboundPacket getFirstPacket(Connection connection) throws IOException {
        int packetLength = decodeFirstVarInt(connection);
        int packetId = decodeFirstVarInt(connection);

        if (packetId == 0x00) { // 00000000 - Login Start
            String name = decodeFirstString(connection);
            boolean hasSignatureData = decodeFirstBoolean(connection);
            if (hasSignatureData) {
                long timestamp = decodeFirstLong(connection);
                byte[] publicKey = decodeFirstByteArray(connection);
                byte[] signature = decodeFirstByteArray(connection);

                return new LoginStart(name, timestamp, publicKey, signature);
            } // else:
            return new LoginStart(name);

        } else if (packetId == 0x01) {
            byte[] sharedSecret = decodeFirstByteArray(connection);
            boolean hasVerifyToken = decodeFirstBoolean(connection);
            if (hasVerifyToken) {
                byte[] verifyToken = decodeFirstByteArray(connection);
                return new EncryptionResponse(sharedSecret, verifyToken);
            } // else:
            long salt = decodeFirstLong(connection);
            byte[] messageSignature = decodeFirstByteArray(connection);
            return new EncryptionResponse(sharedSecret, salt, messageSignature);
        }

        throw new RuntimeException("Unknown packet with ID " + packetId);
    }

    @Override
    protected byte[] encodePacket(ClientboundPacket packet) {
        byte[] content = null;

        byte[] packetId = encodeVarInt(packet.getPacketId());
        byte[] packetLength = encodeVarInt(packetId.length + content.length);

        byte[] encodedPacket = new byte[packetLength.length + packetId.length + content.length];
        System.arraycopy(packetLength, 0, encodedPacket, 0, packetLength.length);
        System.arraycopy(packetId, 0, encodedPacket, packetLength.length, packetId.length);
        System.arraycopy(content, 0, encodedPacket, packetLength.length + packetId.length, content.length);
        return encodedPacket;
    }
}
