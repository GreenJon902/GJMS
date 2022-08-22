package com.greenjon902.gjms;

import com.greenjon902.gjms.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.greenjon902.gjms.Utils.byteArray;
import static com.greenjon902.gjms.Utils.readVarInt;

public class Status_ServerListPing {
    private final String ip = "127.0.0.1";

    @Test
    public void MC1_19() throws IOException {
        // Setup ---
        int port = Utils.makeServer(true);
        System.out.println("Got port " + port);
        Socket socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        long currentTime = System.currentTimeMillis();

        byte[] packets = byteArray(
                // Handshake ------------------------------
                // Length - VarInt
                0x10, // 00010000
                // Packet ID - VarInt
                0x00, // 00000000

                // Protocol Version - VarInt
                0xf6, // 11110110
                0x05, // 00000101
                // Server Address - String (255)
                0x09, // 00001001
                '1', '2', '7', '.', '0', '.', '0', '.', '1',
                // Server Port - Unsigned Short
                0x63, // 01100011
                0xdd, // 11011101
                // Next State - VarInt
                0x01, // 00000001

                // Status Request -------------------------
                // Length - VarInt
                0x01, // 00000001
                // Packet ID - VarInt
                0x00, // 00000000

                // Ping Request -------------------------
                // Length - VarInt
                0x09, // 00000009
                // Packet ID - VarInt
                0x01, // 00000001
                // Payload - Long
                (byte) (currentTime >> 56),
                (byte) (currentTime >> 48),
                (byte) (currentTime >> 40),
                (byte) (currentTime >> 32),
                (byte) (currentTime >> 24),
                (byte) (currentTime >> 16),
                (byte) (currentTime >> 8),
                (byte) currentTime
        );

        // Run ---
        outputStream.write(packets);

        // Check ---
        // Status Response -

        // Packet length
        System.out.println(readVarInt(inputStream)); // TODO: Check packet length

        // Packet ID
        Assertions.assertEquals(0, inputStream.read(), "Wrong PacketId for StatusResponse");

        // JSON Response
        int jsonResponseLength = readVarInt(inputStream);
        byte[] bytes = new byte[jsonResponseLength];
        inputStream.read(bytes);
        String jsonResponse = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("Server Status was " + jsonResponse + " as length was " + jsonResponseLength);
        // TODO: Check json


        // Ping Response -

        // Packet length
        Assertions.assertEquals(9, inputStream.read(), "Wrong PacketLength for PingResponse");

        // Packet ID
        Assertions.assertEquals(1, inputStream.read(), "Wrong PacketId for PingResponse");

        // Payload
        long payload = 0;
        for (int i=0;i<8;i++) {
            payload <<= 8;
            payload |= inputStream.read() & 0xFF;
        }
        Assertions.assertEquals(currentTime, payload, "Wrong Payload for PingResponse");
    }
}
