package com.greenjon902.gjms.connection.prePlay.packetAdapter.status;

import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.Connection;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionState;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.PrePlayPacketAdapter;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.PingResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound.StatusResponse;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.PingRequest;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.serverbound.StatusRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Contains the functionality for converting packets when the connection's {@link PrePlayConnectionState} is
 * {@link PrePlayConnectionState#STATUS} and the protocolVersion is between 0 and 759.
 */
public class StatusPacketAdapter0to759 extends PrePlayPacketAdapter {
    @Override
    public ServerboundPacket decodePacket(InputStream inputStream) throws IOException {
        int packetId = decodeFirstVarInt(inputStream);

        switch (packetId) {
            case 0x00: // 00000000 - Status Request
                return new StatusRequest();
            case 0x01: // 00000001 - Ping Request
                long payload = decodeFirstLong(inputStream);
                return new PingRequest(payload);
            default:
                throw new RuntimeException("Unknown packet with ID " + packetId);
        }
    }

    @Override
    public byte[] encodePacket(ClientboundPacket packet) {
        byte[] content;

        if (packet instanceof StatusResponse statusResponse) {
            byte[] jsonResponse = ("{" +
                    "\"version\":{" +
                    "\"name\":" + statusResponse.versionName + "," +
                    "\"protocol\":" + statusResponse.protocolNumber +
                    "}," +
                    "\"players\":{" +
                    "\"max\":" + statusResponse.maxPlayers + "," +
                    "\"online\":" + statusResponse.onlinePlayers + "," +
                    "\"sample\":" + statusResponse.playerSample + "" +
                    "}," +
                    "\"description\":" + statusResponse.description + "," +
                    ((statusResponse.favicon != null) ? "\"favicon\":\"" + statusResponse.favicon + "\",": "") +
                    "\"previewsChat\":" + statusResponse.previewsChat +
                    "}").getBytes(StandardCharsets.UTF_8);
            byte[] jsonResponseLength = encodeVarInt(jsonResponse.length);
            content = new byte[jsonResponseLength.length + jsonResponse.length];
            System.arraycopy(jsonResponseLength, 0, content, 0, jsonResponseLength.length);
            System.arraycopy(jsonResponse, 0, content, jsonResponseLength.length, jsonResponse.length);

        } else if (packet instanceof PingResponse pingResponse) {
            content = encodeLong(pingResponse.payload);
        } else {
            throw new RuntimeException("StatusPacketAdapter0to759 can only encode StatusRequest and PingRequest packets");
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
