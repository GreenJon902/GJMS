package com.greenjon902.gjms.connection.prePlay.packetAdapter.login;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound.EncryptionRequest;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound.LoginSuccess;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound.SetCompression;
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

        } else if (packetId == 0x01) { // 00000001 - Encryption Response
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
        byte[] content;

        if (packet instanceof EncryptionRequest encryptionRequest) {
            byte[] serverId = encodeString(encryptionRequest.serverId);
            byte[] publicKey = encodeBytes(encryptionRequest.publicKey);
            byte[] verifyToken = encodeBytes(encryptionRequest.verifyToken);

            content = new byte[serverId.length + publicKey.length + verifyToken.length];
            System.arraycopy(serverId, 0, content, 0, serverId.length);
            System.arraycopy(publicKey, 0, content, serverId.length, publicKey.length);
            System.arraycopy(verifyToken, 0, content, serverId.length + publicKey.length,
                    verifyToken.length);

        } else if (packet instanceof LoginSuccess loginSuccess) {
            byte[] uuid = encodeUUID(loginSuccess.uuid);
            byte[] username = encodeString(loginSuccess.username);
            byte[] properties = encodePropertyArray(loginSuccess.properties);

            content = new byte[uuid.length + username.length + properties.length];
            System.arraycopy(uuid, 0, content, 0, uuid.length);
            System.arraycopy(username, 0, content, uuid.length, username.length);
            System.arraycopy(properties, 0, content, uuid.length + username.length, properties.length);

        } else if (packet instanceof SetCompression setCompression) {
            byte[] threshold = encodeVarInt(setCompression.threshold);
            content = threshold;
        } else {
            throw new RuntimeException("LoginPacketAdapter391to759 can only encode EncryptionRequest, LoginSuccess and SetCompression packets");
        }

        byte[] packetId = encodeVarInt(packet.getPacketId());
        byte[] packetLength = encodeVarInt(packetId.length + content.length);

        byte[] encodedPacket = new byte[packetLength.length + packetId.length + content.length];
        System.arraycopy(packetLength, 0, encodedPacket, 0, packetLength.length);
        System.arraycopy(packetId, 0, encodedPacket, packetLength.length, packetId.length);
        System.arraycopy(content, 0, encodedPacket, packetLength.length + packetId.length, content.length);
        return encodedPacket;
    }
}
